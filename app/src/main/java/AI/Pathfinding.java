package AI;



import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import api.Hexagon;
import api.HexagonalGrid;
import api.HexagonalGridCalculator;
import java.util.Comparator;
import java.util.Queue;

public class Pathfinding {


    private final HexagonalGrid hexagonalGrid;
    private final Hexagon destination;                         // Цель
    private final Hexagon start;                               // Начальное положение
    private LinkedList<String> path;                     // Список команд для контроллера (его алгоритм и должен вернуть)
    private final HexagonalGridCalculator calculator;
    private Queue<Unit> openList;               // Список необработанных хексов (тут не все хексы сетки, а только соседние с обработанными хексами)

    // Comparator для открытого списка, чтобы расположить ячейки в порядке возрастания их f
    private Comparator<Unit> fComparator = (Unit unit1, Unit unit2) -> (unit1.f -unit2.f);

    private List<Unit> closedList;                  // Список обработанных хексов




    private  class Unit {
        final Hexagon hexagon;      // рассматриваемый хекс
        Unit mother;  // ссылка на предыдущую ячейку в кратчайшем пути из ячеек от стартовой к этой
        String movement = ""; // Указание для контроллера как добраться из материнской ячейки к этой через его команду
        final int h;         // Цена пути от этой ячейки к целевой (путь берется как прямая)
        int g;              // Цена пути от стартовой ячейки к этой
        int f;                // Общая цена ячейки

        public Unit(Unit mother, Hexagon hexagon)
        {
            this.hexagon = hexagon;
            this.mother = mother;
            h = hFunc();
            g = gFunc();
            f = h + g;
        }

        private int hFunc()  // вычисление h
        {
            return calculator.calculateDistanceBetween(
                    hexagon,
                   destination);
        }

        private int gFunc() // вычисление g
        {
            return mother!=null ? mother.g+1 : 1;

        }


        //Ячейки равны если только равны координаты их хексов
        @Override
        public boolean equals(Object obj) {
            if (getClass() != obj.getClass())
                return false;
            Unit other = (Unit) obj;
            if (other.hexagon.getAxialCoordinate().getGridX() != this.hexagon.getAxialCoordinate().getGridX())
                return false;
            if (other.hexagon.getAxialCoordinate().getGridZ() != this.hexagon.getAxialCoordinate().getGridZ())
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = prime * result + hexagon.getAxialCoordinate().getGridX();
            result = prime * result + hexagon.getAxialCoordinate().getGridZ();
            return result;
        }


    }


    public Pathfinding(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator, Hexagon start, Hexagon destination)
    {
        openList = new PriorityQueue<Unit>(20, fComparator);
        closedList = new ArrayList<Unit>();
        path = new  LinkedList<String>();
        this.hexagonalGrid=hexagonalGrid;
        this.calculator=calculator;
        this.destination=destination;
        this.start = start;
    }


    // Сам цикл поиска
    public  LinkedList<String> findPath()
    {
        Unit startUnit = new Unit(null, start);
        openList.add(startUnit);
        path = checkUnit(openList.poll());
        return path;
    }

    // От ячейки назначения идем до стартовой по ссылкам и собираем команды для контроллера
   private  LinkedList<String> makePath(Unit unit)
    {
        do {
            path.addFirst(unit.movement);
            unit = unit.mother;
        } while (unit.mother!=null);
        return path;
    }

    // Рассмотрение ячейки из открытого списка с наименьшим f и добавлением ее в закрытый список
    private LinkedList <String> checkUnit (Unit unit)
    {
        LinkedList<Hexagon> neighborHex = hexagonalGrid.getNeighborsOf(unit.hexagon);
        for (int i = 0; i<neighborHex.size(); i++)  // Берем 4 соседей ячейки (верхние вроде как не нужны)

            // Если сосед не в закрытом списке и не в препятствиях (надо потом залоченные хексы добавить сразу в закрытый список)
            if (!hexagonalGrid.getLockedHexagons().valueAt(neighborHex.get(i).getAxialCoordinate().getGridZ()).contains(neighborHex.get(i).getAxialCoordinate().getGridX())&&!closedList.contains(neighborHex)) {
                Unit childUnit = new Unit(unit, neighborHex.get(i));
                // если сосед еще не в открытом списке, то просто добавляем его в список
                childUnit = makeCommand(childUnit,i);
                if (!openList.contains(childUnit)) {
                    openList.add(childUnit);
                    // Условие нахождения конечной ячейкм
                    if (childUnit.h==0) return makePath(childUnit); }

                // если сосед уже в открытом списке, то проверяем не нужно ли поменять материскую клетку
                else {
                    // Вот здесь бы доступ к элементу по значению, а не перебор... (стоит позаимствовать чью нибудь структурку данных и заменить PriorityQueue)
                    for (Unit childUnit1 : openList)
                    {
                        if (childUnit.equals(childUnit1)) {
                            if (unit.g < childUnit1.mother.g)
                                childUnit1.mother = unit;
                            break;
                        }
                    }
                }
            }
        // после рассмотрения кладем ячейку в закрытый список
        closedList.add(unit);
        return checkUnit(openList.poll());
    }

    private Unit makeCommand (Unit childUnit, int i)
    {
        switch (i) {
            case 0:
                childUnit.movement = "DOWN_RIGHT";
                break;

            case 1:
                childUnit.movement = "DOWN_LEFT";
                break;

            case 2:
                childUnit.movement = "LEFT";
                break;

            case 3:
                childUnit.movement = "RIGHT";
                break;
        }
        return childUnit;
    }



}
