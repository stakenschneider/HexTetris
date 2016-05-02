package AI;

import android.util.Log;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;
import api.HexagonalGridCalculator;
import java.util.Comparator;
import java.util.Queue;

import static api.CoordinateConverter.convertOffsetCoordinatesToAxialX;
import static api.CoordinateConverter.convertOffsetCoordinatesToAxialZ;
import static api.CoordinateConverter.convertToCol;
import static api.CoordinateConverter.convertToRow;
import static api.AxialCoordinate.fromCoordinates;

public class Pathfinding {

    private final HexagonalGridCalculator calculator;
    private final HexagonalGrid hexagonalGrid;
    private final Hexagon destination , start;             // Цель , Начальное положение
    private LinkedList<String> path;                     // Список команд для контроллера (его алгоритм и должен вернуть)
    private Queue<Unit> openList;               // Список необработанных хексов (тут не все хексы сетки, а только соседние с обработанными хексами)
    protected Comparator<Unit> fComparator = (Unit unit1, Unit unit2) -> (unit1.f -unit2.f);
    private List<Unit> closedList;                  // Список обработанных хексов
    private int dxpivot;
    private int dypivot;

    private  class Unit {
        final Hexagon hexagon;      // рассматриваемый хекс
        Unit mother;  // ссылка на предыдущую ячейку в кратчайшем пути из ячеек от стартовой к этой
        String movement = ""; // Указание для контроллера как добраться из материнской ячейки к этой через его команду
        final int h;         // Цена пути от этой ячейки к целевой (путь берется как прямая)
        int g, f;            // Цена пути от стартовой ячейки к этой , Общая цена ячейки


        public Unit(Unit mother, Hexagon hexagon)
        {
            this.hexagon = hexagon;
            this.mother = mother;
            h = hFunc();
            g = gFunc();
            f = h + g;
        }

        private int hFunc(){ // вычисление h
            return calculator.calculateDistanceBetween(hexagon, destination);
        }

        private int gFunc(){ // вычисление g
            return mother!=null ? mother.g+1 : 1;
        }


        @Override
        public boolean equals(Object obj) {        //Ячейки равны если только равны координаты их хексов
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
            int prime = 31 , result = 1;
            result = prime * result + hexagon.getAxialCoordinate().getGridX();
            result = prime * result + hexagon.getAxialCoordinate().getGridZ();
            return result;
        }
    }


    public Pathfinding(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator, Hexagon start, Hexagon destination, Hexagon pivot)
    {
        openList = new PriorityQueue<>(20, fComparator);
        closedList = new ArrayList<>();
        path = new  LinkedList<>();
        this.hexagonalGrid=hexagonalGrid;
        this.calculator=calculator;
        this.destination=destination;
        this.start = start;
        this.dxpivot = convertToCol(start.getGridX(),start.getGridZ())-convertToCol(pivot.getGridX(), pivot.getGridZ());
        this.dypivot = convertToRow(start.getGridZ())-convertToRow(pivot.getGridZ());
    }


    public  LinkedList<String> findPath()     // Сам цикл поиска
    {
        Unit startUnit = new Unit(null, start);
        openList.add(startUnit);
        path = checkUnit(openList.poll());
        return path;
    }


    private  LinkedList<String> makePath(Unit unit)    // От ячейки назначения идем до стартовой по ссылкам и собираем команды для контроллера
    {
        do {
            path.addFirst(unit.movement);
            unit = unit.mother;
        } while (unit.mother!=null);
        return path;
    }


    private LinkedList <String> checkUnit (Unit unit)    // Рассмотрение ячейки из открытого списка с наименьшим f и добавлением ее в закрытый список
    {
        final int col, row;
        LinkedList<Hexagon> neighborHex = hexagonalGrid.getNeighborsOf(unit.hexagon);

        row = convertToRow(unit.hexagon.getGridZ())-dypivot;
        if (row%2 != 0 & dypivot != 0) col = convertToCol( unit.hexagon.getGridX(), unit.hexagon.getGridZ()) - dxpivot - 1;
        else col = convertToCol(unit.hexagon.getGridX(), unit.hexagon.getGridZ()) - dxpivot;
        AxialCoordinate pivotCoordinate = fromCoordinates(convertOffsetCoordinatesToAxialX(col, row), convertOffsetCoordinatesToAxialZ(row));
        final int x = pivotCoordinate.getGridX();
        final int z = pivotCoordinate.getGridZ();
        final int y =  - x - z;
        AxialCoordinate  counterClockwisePosition =  fromCoordinates(-(-unit.hexagon.getGridX() - unit.hexagon.getGridZ() - y) + x,-(unit.hexagon.getGridX() - x) + z);
        AxialCoordinate clockwisePosition = fromCoordinates(-(unit.hexagon.getGridZ() - z) + x, -(-unit.hexagon.getGridX() - unit.hexagon.getGridZ() - y) + z);
        if (hexagonalGrid.containsAxialCoordinate(clockwisePosition)) {
            neighborHex.add(hexagonalGrid.getByAxialCoordinate(clockwisePosition).get());
        }
        else {
            neighborHex.add(null);
        }
        if (hexagonalGrid.containsAxialCoordinate(counterClockwisePosition)){
            neighborHex.add(hexagonalGrid.getByAxialCoordinate(counterClockwisePosition).get());
        }
        else{
            neighborHex.add(null);
        }

        for (int i = 0; i<neighborHex.size(); i++)  // Берем 4 соседей ячейки (верхние вроде как не нужны) + 2 позиции после поворота

            // Если сосед не в закрытом списке и не в препятствиях (надо потом залоченные хексы добавить сразу в закрытый список)
            if (neighborHex.get(i)!=null && !hexagonalGrid.getLockedHexagons().valueAt(neighborHex.get(i).getAxialCoordinate().getGridZ()).contains(neighborHex.get(i).getAxialCoordinate().getGridX())
                    &&!closedList.contains(neighborHex)) {
                Unit childUnit = new Unit(unit, neighborHex.get(i));
                childUnit = makeCommand(childUnit,i);
                if (!openList.contains(childUnit)) {   // если сосед еще не в открытом списке, то просто добавляем его в список
                    openList.add(childUnit);
                    if (childUnit.h==0) return makePath(childUnit); }     // Условие нахождения конечной ячейкм

                // если сосед уже в открытом списке, то проверяем не нужно ли поменять материскую клетку
                else {
                    // Вот здесь бы доступ к элементу по значению, а не перебор... (стоит позаимствовать чью нибудь структурку данных и заменить PriorityQueue)
                    for (Unit childUnit1 : openList)
                        if (childUnit.equals(childUnit1)&&(unit.g < childUnit1.mother.g)) {
                            childUnit1.mother = unit;
                            childUnit1.g = unit.g+1;
                            childUnit1.movement = childUnit.movement;
                            break;
                        }
                }
            }
        closedList.add(unit);        // после рассмотрения кладем ячейку в закрытый список
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
            case 4:
                childUnit.movement = "CLCK";
                break;
            case 5:
                childUnit.movement = "COUNTER_CLCK";
                break;
        }
        return childUnit;
    }
}