package AI;


import android.util.Log;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.PriorityQueue;

import api.Hexagon;
import api.HexagonalGrid;
import api.HexagonalGridCalculator;
import java.util.Comparator;

public class Pathfinding {


    private final HexagonalGrid hexagonalGrid;
    private Hexagon destination;                         // Цель
    private Hexagon start;                               // Начальное положение
    private LinkedList<String> path;                     // Список команд для контроллера (его алгоритм и должен вернуть)
    private final HexagonalGridCalculator calculator;
    private PriorityQueue<Unit>  openList;               // Список необработанных хексов (тут не все хексы сетки, а только соседние с обработанными хексами)

    // Comparator для открытого списка, чтобы расположить ячейки в порядке возрастания их f
    private Comparator<Unit> fComparator = (Unit unit1, Unit unit2) -> (unit1.f -unit2.f);



    private ArrayList<Unit> closedList;                  // Список обработанных хексов, еще здесь хранятся залоченные хексы с сетки
                                                         // (быть может не стоит их сюда добавлять, а сразу спрашивать у hexagonal grid - пустой это хекс или нет)



    private class Unit {
        Hexagon hexagon;      // рассматриваемый хекс
        public Unit mother;  // ссылка на предыдущую ячейку в кратчайшем пути из ячеек от стартовой к этой
        public String movement = ""; // Указание для контроллера как добраться из материнской ячейки к этой через его команду
        public final int h;         // Цена пути от этой ячейки к целевой (путь берется как прямая)
        public int g;              // Цена пути от стартовой ячейки к этой
        public int f;                // Общая цена ячейки

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

    }


    public Pathfinding(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator)
    {
        openList = new <Unit> PriorityQueue(20, fComparator);
        closedList = new ArrayList<Unit>();
        path = new  LinkedList<String>();
        this.hexagonalGrid=hexagonalGrid;
        this.calculator=calculator;
    }


    // Установка начальных условий
    public void setConditions(Hexagon destination, Hexagon start)
    {
        this.destination=destination;
        this.start = start;
    }

    // Сам цикл поиска
    public  LinkedList<String> findPath()
    {
        Unit startUnit = new Unit(null, start);
        openList.add(startUnit);
        boolean end = false;
        while (end==false) end = checkUnit(openList.poll());
        path = makePath();
        return path;
    }

    // От ячейки назначения идем до стартовой по ссылкам и собираем команды для контроллера
   private  LinkedList<String> makePath()
    {
        Unit unit = openList.peek();
        do {
            path.addFirst(unit.movement);
            unit = unit.mother;
        } while (unit.mother!=null);
        return path;
    }

    // Рассмотрение ячейки из открытого списка с наименьшим f и добавлением ее в закрытый список
    private boolean checkUnit (Unit unit)
    {
        LinkedList<Hexagon> neighborHex = hexagonalGrid.getNeighborsOf(unit.hexagon);
        for (int i = 0; i<neighborHex.size(); i++)  // Берем 4 соседей ячейки (верхние вроде как не нужны)

            // Если сосед не в закрытом списке и не в препятствиях (надо потом залоченные хексы добавить сразу в закрытый список)
            if (!hexagonalGrid.getLockedHexagons().valueAt(neighborHex.get(i).getAxialCoordinate().getGridZ()).contains(neighborHex.get(i).getAxialCoordinate().getGridX())) {
                Unit childUnit = new Unit(unit, neighborHex.get(i));

                // Команды как пройти к этому хексу от материнского
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

                // если сосед еще не в открытом списке, то просто добавляем его в список
                if (!openList.contains(childUnit)) {
                    openList.add(childUnit);
                    // Условие нахождения конечной ячейкм
                    if (childUnit.h==0) return true;}

                // если сосед уже в открытом списке, то проверяем не нужно ли поменять материскую клетку
                else {
                    // Вот здесь бы доступ к элементу по значению, а не перебор... (стоит позаимствовать чью нибудь структурку данных и заменить PriorityQueue)
                    for (Unit childUnit1 : openList)
                    {
                        if (childUnit.equals(childUnit1)) {childUnit.g = childUnit1.g; break;}
                    }
                    if (unit.g <= childUnit.mother.g){
                        openList.remove(childUnit);
                        openList.add(new Unit(unit, neighborHex.get(i)));
                    }
                }
            }
        // после рассмотрения кладем ячейку в закрытый список
        closedList.add(unit);
        return false;
    }

}
