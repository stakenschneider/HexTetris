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
    ArrayList<Hexagon> destination;
    ArrayList<Hexagon> start;
    private LinkedList<String> path;                     // Список команд для контроллера (его алгоритм и должен вернуть)
    private Queue<ComplexFigure> openList;               // Список необработанных хексов (тут не все хексы сетки, а только соседние с обработанными хексами)
    protected Comparator<ComplexFigure> fComparator = (ComplexFigure figure1, ComplexFigure figure2) -> (figure1.f - figure2.f);
    private List<ComplexFigure> closedList;                  // Список обработанных хексов
    private int dxpivot ;
    private int dypivot;


    public Pathfinding(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator, ArrayList<Hexagon> start, ArrayList<Hexagon> destination, Hexagon pivot) {
        this.start = start;
        this.destination =  destination;
        openList = new PriorityQueue<>(20, fComparator);
        closedList = new ArrayList<>();
        path = new LinkedList<>();
        this.hexagonalGrid = hexagonalGrid;
        this.calculator = calculator;
        dxpivot= convertToCol(start.get(0).getGridX(), start.get(0).getGridZ()) - convertToCol(pivot.getGridX(), pivot.getGridZ());
        dypivot= convertToRow(start.get(0).getGridZ()) - convertToRow(pivot.getGridZ());
    }

    private class Unit {
        final Hexagon hexagon;      // рассматриваемый хекс
        Unit mother;  // ссылка на предыдущую ячейку в кратчайшем пути из ячеек от стартовой к этой
        String movement = ""; // Указание для контроллера как добраться из материнской ячейки к этой через его команду
        final int h;         // Цена пути от этой ячейки к целевой (путь берется как прямая)
        int g, f;            // Цена пути от стартовой ячейки к этой , Общая цена ячейки
        int number;           // Номер юнита в фигуре


        public Unit(Unit mother, Hexagon hexagon, int number) {
            this.hexagon = hexagon;
            this.mother = mother;
            this.number = number;
            h = hFunc();
            g = gFunc();
            f = h + g;

        }

        private int hFunc() { // вычисление h
            return calculator.calculateDistanceBetween(hexagon, destination.get(number));
        }

        private int gFunc() { // вычисление g
            return mother != null ? mother.g + 1 : 1;
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
            int prime = 31, result = 1;
            result = prime * result + hexagon.getAxialCoordinate().getGridX();
            result = prime * result + hexagon.getAxialCoordinate().getGridZ();
            return result;
        }
    }

    private class ComplexFigure {
        List<Unit> units;
        ComplexFigure mother;
        String movement = ""; // Указание для контроллера как добраться из материнской ячейки к этой через его команду
        int h;         // Цена пути от этой ячейки к целевой (путь берется как прямая)
        int g, f;            // Цена пути от стартовой ячейки к этой , Общая цена ячейки

        public ComplexFigure(List<Unit> units, ComplexFigure mother) {
            h = 0;
            this.units = units;
            this.mother = mother;
            for (Unit unit : units) {
                h = h + unit.h;
                if (mother!=null) g = 1 + mother.g;
                else  g=1;
            }
            f = h + g;
        }

        @Override
        public boolean equals(Object obj) {        //Фигуры равны если только равны все их ячейки
            if (getClass() != obj.getClass())
                return false;

            ComplexFigure other = (ComplexFigure) obj;

            if (units.size() != other.units.size())
                return false;

            for (int i = 0; i < units.size(); i++)
                if (units.get(i) != other.units.get(i))
                    return false;
            return true;
        }

        @Override
        public int hashCode() {
            int prime = 31, result = 1;
            for (Unit unit : units) {
                result = prime * result + unit.hexagon.getAxialCoordinate().getGridX();
                result = prime * result + unit.hexagon.getAxialCoordinate().getGridZ();
            }
            return result;
        }

    }



    public LinkedList<String> findPath()     // Сам цикл поиска
    {
        ArrayList<Unit> startUnits = new ArrayList<Unit>();
        for (int i = 0; i < start.size(); i++) {
            Unit unit = new Unit(null, start.get(i), i);
            startUnits.add(unit);
        }
        ComplexFigure startFigure = new ComplexFigure(startUnits, null);
        openList.add(startFigure);
        path = checkFigure(openList.poll());
        return path;
    }


    private LinkedList<String> makePath(ComplexFigure figure)    // От ячейки назначения идем до стартовой по ссылкам и собираем команды для контроллера
    {
        do {
            path.addFirst(figure.movement);
            figure = figure.mother;
        } while (figure.mother != null);
        return path;
    }


    private LinkedList<String> checkFigure(ComplexFigure figure)    // Рассмотрение ячейки из открытого списка с наименьшим f и добавлением ее в закрытый список
    {
        Log.d("dsa", Integer.toString(figure.h));
        LinkedList<ComplexFigure> neighborFigures = new LinkedList<ComplexFigure>();
        ComplexFigure figureDownRight = moveDownRight(figure);
        ComplexFigure figureDownLeft = moveDownLeft(figure);
        ComplexFigure figureRight = moveRight(figure);
        ComplexFigure figureLeft = moveLeft(figure);
        AxialCoordinate pivot = newPivot(figure);
        ComplexFigure figureClockwise = clockwise(figure, pivot);
        ComplexFigure figureCounterClockwise = counterClockwise(figure,pivot);
        neighborFigures.add(figureDownRight);
        neighborFigures.add(figureDownLeft);
        neighborFigures.add(figureLeft);
        neighborFigures.add(figureRight);
        neighborFigures.add(figureClockwise);
        neighborFigures.add(figureCounterClockwise);

        for (int i = 0; i < neighborFigures.size(); i++)  // Берем 4 соседей ячейки (верхние вроде как не нужны) + 2 позиции после поворота
            if (neighborFigures.get(i) != null && checkUnits(neighborFigures.get(i).units))
            {
                ComplexFigure childFigure = new  ComplexFigure(neighborFigures.get(i).units,figure);
                childFigure = makeCommand(childFigure, i);
                if (!openList.contains( childFigure)) {   // если сосед еще не в открытом списке, то просто добавляем его в список
                    openList.add(childFigure);
                    if ( childFigure.h == 0) return makePath(childFigure);
                }     // Условие нахождения конечной ячейкм
                // если сосед уже в открытом списке, то проверяем не нужно ли поменять материскую клетку
                else {
                    // Вот здесь бы доступ к элементу по значению, а не перебор... (стоит позаимствовать чью нибудь структурку данных и заменить PriorityQueue)
                    for (ComplexFigure childFigure1 : openList)
                        if (childFigure.equals(childFigure1) && (figure.g < childFigure1.mother.g)) {
                            childFigure1.mother = figure;
                            childFigure1.g = figure.g + 1;
                            childFigure1.movement = childFigure.movement;
                            break;
                        }
                }
            }
        closedList.add(figure);        // после рассмотрения кладем ячейку в закрытый список
        return checkFigure(openList.poll());
    }

    private boolean checkUnits (List<Unit> units)
    {
        for (Unit unit : units)
        {
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getAxialCoordinate().getGridZ()).contains(unit.hexagon.getAxialCoordinate().getGridX())
                    || closedList.contains(unit.hexagon))
                return false;

        }
        return true;
    }


    private ComplexFigure moveDownRight(ComplexFigure figure) {
        List<Unit> units = new ArrayList<Unit>();
        for (Unit unit : figure.units) {
            if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getAxialCoordinate().getGridX(),unit.hexagon.getAxialCoordinate().getGridZ() + 1)))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getAxialCoordinate().getGridZ() + 1).contains(unit.hexagon.getAxialCoordinate().getGridX()))
                return null;
            units.add(new Unit(unit, hexagonalGrid.getByAxialCoordinate(fromCoordinates(unit.hexagon.getGridX(), unit.hexagon.getGridZ() + 1)).get(), unit.number));
        }
        ComplexFigure newFigure = new ComplexFigure(units,figure);
        return newFigure;
    }

    private AxialCoordinate newPivot (ComplexFigure figure)
    {
        final int col, row;
        row = convertToRow(figure.units.get(0).hexagon.getGridZ())-dypivot;
        if (row % 2 != 0 & dypivot != 0)
            col = convertToCol(figure.units.get(0).hexagon.getGridX(), figure.units.get(0).hexagon.getGridZ()) - dxpivot - 1;
        else col = convertToCol(figure.units.get(0).hexagon.getGridX(), figure.units.get(0).hexagon.getGridZ()) - dxpivot;
        AxialCoordinate pivotCoordinate = fromCoordinates(convertOffsetCoordinatesToAxialX(col, row), convertOffsetCoordinatesToAxialZ(row));
        return pivotCoordinate;
    }

    private ComplexFigure clockwise(ComplexFigure figure, AxialCoordinate pivot)
    {
        final int x,y,z;
        x = pivot.getGridX();
        z = pivot.getGridZ();
        y = - x - z;
        List<Unit> units = new ArrayList<Unit>();
        for (Unit unit:figure.units) {
            AxialCoordinate clockwisePosition = fromCoordinates(-(unit.hexagon.getGridZ() - z) + x, -(-unit.hexagon.getGridX() - unit.hexagon.getGridZ() - y) + z);
            if(!hexagonalGrid.containsAxialCoordinate(clockwisePosition))
                return null;
            if ( hexagonalGrid.getLockedHexagons().valueAt(clockwisePosition.getGridZ()).contains(clockwisePosition.getGridX()))
                return null;
            units.add(new Unit(unit, hexagonalGrid.getByAxialCoordinate(clockwisePosition).get(), unit.number));
        }
        ComplexFigure newFigure = new ComplexFigure(units,figure);
        return newFigure;
    }

    private ComplexFigure counterClockwise(ComplexFigure figure, AxialCoordinate pivot)
    {
        final int x,y,z;
        x = pivot.getGridX();
        z = pivot.getGridZ();
        y = - x - z;
        List<Unit> units = new ArrayList<Unit>();
        for (Unit unit:figure.units) {
            AxialCoordinate counterClockwisePosition =  fromCoordinates(-(-unit.hexagon.getGridX() - unit.hexagon.getGridZ() - y) + x, -(unit.hexagon.getGridX() - x) + z);
            if(!hexagonalGrid.containsAxialCoordinate(counterClockwisePosition))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(counterClockwisePosition.getGridZ()).contains(counterClockwisePosition.getGridX()))
                return null;
            units.add(new Unit(unit, hexagonalGrid.getByAxialCoordinate(counterClockwisePosition).get(), unit.number));
        }
        ComplexFigure newFigure = new ComplexFigure(units,figure);
        return newFigure;
    }

    private ComplexFigure moveDownLeft(ComplexFigure figure) {
        List<Unit> units = new ArrayList<Unit>();
        for (Unit unit : figure.units) {
            if  (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getAxialCoordinate().getGridX()-1,unit.hexagon.getAxialCoordinate().getGridZ() + 1)))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getAxialCoordinate().getGridZ() + 1).contains(unit.hexagon.getAxialCoordinate().getGridX()-1))
                return null;
            units.add(new Unit(unit, hexagonalGrid.getByAxialCoordinate(fromCoordinates(unit.hexagon.getGridX()-1, unit.hexagon.getGridZ() + 1)).get(), unit.number));
        }
        ComplexFigure newFigure = new ComplexFigure(units,figure);
        return newFigure;
    }
    private ComplexFigure moveLeft(ComplexFigure figure) {
        List<Unit> units = new ArrayList<Unit>();
        for (Unit unit : figure.units) {
            if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getAxialCoordinate().getGridX()-1,unit.hexagon.getAxialCoordinate().getGridZ())))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getAxialCoordinate().getGridZ()).contains(unit.hexagon.getAxialCoordinate().getGridX()-1))
                return null;
            units.add(new Unit(unit, hexagonalGrid.getByAxialCoordinate(fromCoordinates(unit.hexagon.getGridX()-1, unit.hexagon.getGridZ())).get(), unit.number));
        }
        ComplexFigure newFigure = new ComplexFigure(units,figure);
        return newFigure;
    }
    private ComplexFigure moveRight(ComplexFigure figure) {
        List<Unit> units = new ArrayList<Unit>();
        for (Unit unit : figure.units) {
            if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getAxialCoordinate().getGridX()+1,unit.hexagon.getAxialCoordinate().getGridZ())))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getAxialCoordinate().getGridZ()).contains(unit.hexagon.getAxialCoordinate().getGridX()))
                return null;
            units.add(new Unit(unit, hexagonalGrid.getByAxialCoordinate(fromCoordinates(unit.hexagon.getGridX()+1, unit.hexagon.getGridZ())).get(), unit.number));
        }
        ComplexFigure newFigure = new ComplexFigure(units,figure);
        return newFigure;
    }



    private ComplexFigure makeCommand (ComplexFigure childFigure, int i)
    {
        switch (i) {
            case 0:
                childFigure.movement = "DOWN_RIGHT";
                break;

            case 1:
                childFigure.movement = "DOWN_LEFT";
                break;

            case 2:
                childFigure.movement = "LEFT";
                break;

            case 3:
                childFigure.movement = "RIGHT";
                break;
            case 4:
                childFigure.movement = "CLCK";
                break;
            case 5:
                childFigure.movement = "COUNTER_CLCK";
                break;
        }
        return childFigure;
    }
}