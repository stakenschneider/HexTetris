package AI;
        import java.util.ArrayList;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.PriorityQueue;
        import api.AxialCoordinate;
        import api.HexagonalGrid;
        import api.HexagonalGridCalculator;
        import java.util.Comparator;
        import java.util.Queue;

        import static api.AxialCoordinate.fromCoordinates;

public class Pathfinding {

    private final HexagonalGridCalculator calculator;
    private final HexagonalGrid hexagonalGrid;
    ArrayList<AxialCoordinate> destination;
    ArrayList<AxialCoordinate> start;
    private LinkedList<String> path;                     // Список команд для контроллера (его алгоритм и должен вернуть)
    private Queue<ComplexFigure> openList;               // Список необработанных хексов (тут не все хексы сетки, а только соседние с обработанными хексами)
    protected Comparator<ComplexFigure> fComparator = (ComplexFigure figure1, ComplexFigure figure2) -> (figure1.f - figure2.f);
    private List<ComplexFigure> closedList;                  // Список обработанных хексов
    private AxialCoordinate pivot;
    private double max;

    public Pathfinding(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator, ArrayList<AxialCoordinate> start, ArrayList<AxialCoordinate> destination, AxialCoordinate pivot) {
        this.start = start;
        this.destination =  destination;
        openList = new PriorityQueue<>(20, fComparator);
        closedList = new ArrayList<>();
        path = new LinkedList<>();
        this.hexagonalGrid = hexagonalGrid;
        // Ограничение в 70 процентов поля
        max = hexagonalGrid.getHeight()*hexagonalGrid.getWidth()*0.7;
        this.calculator = calculator;
        this.pivot = pivot;
    }

    private class Unit {
        final AxialCoordinate hexagon;      // рассматриваемый хекс
        Unit mother;                       // ссылка на предыдущую ячейку в кратчайшем пути из ячеек от стартовой к этой
        final int h;                       // Цена пути от этой ячейки к целевой (путь берется как прямая)
        int number;           // Номер юнита в фигуре


        public Unit(Unit mother, AxialCoordinate hexagon, int number) {
            this.hexagon = hexagon;
            this.mother = mother;
            this.number = number;
            h = hFunc();

        }

        private int hFunc() { // вычисление h
            return calculator.calculateDistanceBetween(hexagonalGrid.getByAxialCoordinate(hexagon).get(),
                    hexagonalGrid.getByAxialCoordinate(destination.get(number)).get());
        }

        @Override
        public boolean equals(Object obj) {        //Ячейки равны если только равны координаты их хексов
            if (getClass() != obj.getClass())
                return false;

            Unit other = (Unit) obj;

            if (other.hexagon.getGridX() != this.hexagon.getGridX())
                return false;
            if (other.hexagon.getGridZ() != this.hexagon.getGridZ())
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            int prime = 31, result = 1;
            result = prime * result + hexagon.getGridX();
            result = prime * result + hexagon.getGridZ();
            return result;
        }
    }

    private class ComplexFigure {
        List<Unit> units;
        AxialCoordinate pivot;
        ComplexFigure mother;
        String movement = "";
        int h;
        int g, f;

        public ComplexFigure(List<Unit> units, ComplexFigure mother, AxialCoordinate pivot) {
            h = 0;
            this.units = units;
            this.mother = mother;
            for (Unit unit : units) {
                h = h + unit.h;
                if (mother!=null) g = 1 + mother.g;
                else  g=1;
            }
            f = h + g;
            this.pivot = pivot;
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
                result = prime * result + unit.hexagon.getGridX();
                result = prime * result + unit.hexagon.getGridZ();
            }
            return result;
        }
    }



    public LinkedList<String> findPath(){     // Сам цикл поиска
        for (AxialCoordinate coordinate : destination)
            if (!hexagonalGrid.containsAxialCoordinate(coordinate))
                return path;
        ArrayList<Unit> startUnits = new ArrayList<>();
        for (int i = 0; i < start.size(); i++) {
            Unit unit = new Unit(null, start.get(i), i);
            startUnits.add(unit);
        }
        ComplexFigure startFigure = new ComplexFigure(startUnits, null, pivot);
        openList.add(startFigure);
        path = checkFigure(openList.poll());
        return path;
    }


    private LinkedList<String> makePath(ComplexFigure figure){    // От ячейки назначения идем до стартовой по ссылкам и собираем команды для контроллера
        do {
            path.addFirst(figure.movement);
            figure = figure.mother;
        } while (figure != null);

        return path;
    }


    private LinkedList<String> checkFigure(ComplexFigure figure){    // Рассмотрение ячейки из открытого списка с наименьшим f и добавлением ее в закрытый список
        LinkedList<ComplexFigure> neighborFigures = new LinkedList<>();
        ComplexFigure figureDownRight = moveDownRight(figure);
        ComplexFigure figureDownLeft = moveDownLeft(figure);
        ComplexFigure figureRight = moveRight(figure);
        ComplexFigure figureLeft = moveLeft(figure);
        ComplexFigure figureClockwise = clockwise(figure);
        ComplexFigure figureCounterClockwise = counterClockwise(figure);
        neighborFigures.add(figureDownRight);
        neighborFigures.add(figureDownLeft);
        neighborFigures.add(figureLeft);
        neighborFigures.add(figureRight);
        neighborFigures.add(figureClockwise);
        neighborFigures.add(figureCounterClockwise);

        for (int i = 0; i < neighborFigures.size(); i++) {
            if (neighborFigures.get(i) != null && checkUnits(neighborFigures.get(i).units)) {
                ComplexFigure childFigure = new ComplexFigure(neighborFigures.get(i).units, figure, neighborFigures.get(i).pivot);
                childFigure = makeCommand(childFigure, i);
                if (!openList.contains(childFigure)) {
                    openList.add(childFigure);
                    if (childFigure.h == 0) { return makePath(childFigure); }
                } else {
                }
            }
        }
        closedList.add(figure);
        if (closedList.size() == max||openList.size()==0) return null;
        return checkFigure(openList.poll());
    }


    private boolean checkUnits (List<Unit> units) {
        for (Unit unit : units) {
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getGridZ()).contains(unit.hexagon.getGridX()) || closedList.contains(unit.hexagon))
                return false;
        }
        return true;
    }


    private ComplexFigure moveDownRight(ComplexFigure figure) {
        List<Unit> units = new ArrayList<>();
        for (Unit unit : figure.units) {
            if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getGridX(),unit.hexagon.getGridZ() + 1)))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getGridZ() + 1).contains(unit.hexagon.getGridX()))
                return null;
            units.add(new Unit(unit,fromCoordinates(unit.hexagon.getGridX(), unit.hexagon.getGridZ() + 1), unit.number));
        }

        return new ComplexFigure(units,figure, fromCoordinates(figure.pivot.getGridX(),figure.pivot.getGridZ()+1));
    }


    private ComplexFigure clockwise(ComplexFigure figure) {
        final int x,y,z;
        x = figure.pivot.getGridX();
        z = figure.pivot.getGridZ();
        y = - x - z;
        List<Unit> units = new ArrayList<>();
        for (Unit unit:figure.units) {
            AxialCoordinate clockwisePosition = fromCoordinates(-(unit.hexagon.getGridZ() - z) + x, -(-unit.hexagon.getGridX() - unit.hexagon.getGridZ() - y) + z);
            if(!hexagonalGrid.containsAxialCoordinate(clockwisePosition))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(clockwisePosition.getGridZ())!=null && hexagonalGrid.getLockedHexagons().valueAt(clockwisePosition.getGridZ()).contains(clockwisePosition.getGridX()))
                return null;
            units.add(new Unit(unit, clockwisePosition, unit.number));
        }
        return new ComplexFigure(units,figure,fromCoordinates(figure.pivot.getGridX(),figure.pivot.getGridZ()));
    }


    private ComplexFigure counterClockwise(ComplexFigure figure) {
        final int x,y,z;
        x = figure.pivot.getGridX();
        z = figure.pivot.getGridZ();
        y = - x - z;
        List<Unit> units = new ArrayList<>();
        for (Unit unit:figure.units) {
            AxialCoordinate counterClockwisePosition =  fromCoordinates(-(-unit.hexagon.getGridX() - unit.hexagon.getGridZ() - y) + x, -(unit.hexagon.getGridX() - x) + z);
            if(!hexagonalGrid.containsAxialCoordinate(counterClockwisePosition))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(counterClockwisePosition.getGridZ()).contains(counterClockwisePosition.getGridX()))
                return null;
            units.add(new Unit(unit, counterClockwisePosition, unit.number));
        }
        return new ComplexFigure(units,figure, fromCoordinates(figure.pivot.getGridX(),figure.pivot.getGridZ()));
    }


    private ComplexFigure moveDownLeft(ComplexFigure figure) {
        List<Unit> units = new ArrayList<>();
        for (Unit unit : figure.units) {
            if  (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getGridX()-1,unit.hexagon.getGridZ() + 1)))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getGridZ() + 1).contains(unit.hexagon.getGridX()-1))
                return null;
            units.add(new Unit(unit, fromCoordinates(unit.hexagon.getGridX()-1, unit.hexagon.getGridZ() + 1), unit.number));
        }
        return new ComplexFigure(units,figure, fromCoordinates(figure.pivot.getGridX()-1,figure.pivot.getGridZ()+1));
    }


    private ComplexFigure moveLeft(ComplexFigure figure) {
        List<Unit> units = new ArrayList<>();
        for (Unit unit : figure.units) {
            if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getGridX()-1,unit.hexagon.getGridZ())))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getGridZ()).contains(unit.hexagon.getGridX()-1))
                return null;
            units.add(new Unit(unit, fromCoordinates(unit.hexagon.getGridX()-1, unit.hexagon.getGridZ()), unit.number));
        }
        return new ComplexFigure(units,figure, fromCoordinates(figure.pivot.getGridX()-1,figure.pivot.getGridZ()));
    }


    private ComplexFigure moveRight(ComplexFigure figure) {
        List<Unit> units = new ArrayList<>();
        for (Unit unit : figure.units) {
            if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(unit.hexagon.getGridX()+1,unit.hexagon.getGridZ())))
                return null;
            if (hexagonalGrid.getLockedHexagons().valueAt(unit.hexagon.getGridZ()).contains(unit.hexagon.getGridX()+1))
                return null;
            units.add(new Unit(unit,fromCoordinates(unit.hexagon.getGridX()+1, unit.hexagon.getGridZ()), unit.number));
        }
        return new ComplexFigure(units,figure, fromCoordinates(figure.pivot.getGridX()+1,figure.pivot.getGridZ()));
    }


    private ComplexFigure makeCommand (ComplexFigure childFigure, int i) {
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