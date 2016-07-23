package AI;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;
import api.HexagonalGridCalculator;

import static api.AxialCoordinate.fromCoordinates;


public class Mephistopheles {
    public SparseArray<ArrayList<Integer>> lockedHexagons;
    public HexagonalGrid hexagonalGrid;
    protected Comparator<Position> comparator = (Position pos1, Position pos2) -> (pos2.priority - pos1.priority); // Компаратор для сортировки позиций по их приоритету (по убыванию)
    private Queue<Position> positions; // позиции размещенные по убыванию
    HexagonalGridCalculator calculator;
    LinkedList<String> path; // Возвращаемый путь для Controller


    public Mephistopheles(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator) {
        this.hexagonalGrid = hexagonalGrid;
        this.lockedHexagons = hexagonalGrid.getLockedHexagons();
        positions = new PriorityQueue<>(20, comparator);
        this.calculator = calculator;
    }

    public LinkedList<String> startSearch(ArrayList<AxialCoordinate> hexs) {

        ArrayList<AxialCoordinate> start = new ArrayList<>();
        for (AxialCoordinate coordinate : hexs)
            try {
                start.add(coordinate.clone());
            } catch (CloneNotSupportedException e) {
            }

        ComplexFigure figure = new ComplexFigure(start);

        ArrayList<AxialCoordinate> axialCoordinates = new ArrayList<>();
        hexagonalGrid.getHexagons().forEach((Hexagon hexagon) -> axialCoordinates.add(hexagon.getAxialCoordinate()));

        for (AxialCoordinate coordinate : axialCoordinates) {
            for (int i =0; i<figure.states.size(); i++) {
                if (figure.states.get(i) != null) {
                    Position position = new Position(figure.states.get(i), coordinate, i);

                    if (position.isValid()) {
                        position.makePriority();

                        if (position.priority > 0)
                            positions.add(position);

                    }
                }
            }
        }

        // Так как позиции расположены с самой лучшей, то берем первую и пытаемся проложить путь, если не получилось, то берем следующую и повторяем

        AxialCoordinate pivot = new AxialCoordinate(0,0);
        final AxialCoordinate finalPivot = fromCoordinates(start.get(0).getGridX(),start.get(0).getGridZ());
        start.remove(0);
        while (path == null) {
            if (positions.size() == 0)
                return path;
            Position pos = positions.poll();
            pivot.setCoordinate(pos.coordinates.get(0).getGridX() - figure.pivots.get(pos.state).getGridX(), pos.coordinates.get(0).getGridZ() - figure.pivots.get(pos.state).getGridZ());
            Log.d("Checkit", Integer.toString(pivot.getGridX()) + "   "+ Integer.toString(pivot.getGridZ()) + " Unit  " +Integer.toString(pos.coordinates.get(0).getGridX()) + "   "+ Integer.toString(pos.coordinates.get(0).getGridZ()) );
            Pathfinding pathfinding = new Pathfinding(hexagonalGrid, calculator, pos.coordinates, start, pivot, finalPivot);
            path = pathfinding.findPath();
        }
        return path;
    }


    private class ComplexFigure {   // класс для работы с фигурой для которой ищем наилучшую позицию
        ArrayList<ArrayList<AxialCoordinate>> states; //6 положений
        ArrayList<AxialCoordinate> pivots;

        public ComplexFigure(ArrayList<AxialCoordinate> hexs) {
            states = new ArrayList<>();
            pivots = new ArrayList<>();
            makeStates(hexs);
        }

        private void makeStates(ArrayList<AxialCoordinate> hexs) {  // создаем 6 положений фигуры
            ArrayList<AxialCoordinate> firstState = new ArrayList<>();
            for (AxialCoordinate coordinate : hexs)
                try {
                    firstState.add(coordinate.clone());
                } catch (CloneNotSupportedException e) {}

            //Взяли координаты точки поворота
            int x = firstState.get(0).getGridX(), z = firstState.get(0).getGridZ(), y = -x - z;
            pivots.add(fromCoordinates(firstState.get(1).getGridX() -x,firstState.get(1).getGridZ() - z));
            firstState.remove(0);  // убрал точку поворота, так как она больше уже не нужна
            firstState.trimToSize();
            states.add(firstState);
            if (firstState.size() > 1) {
                for (int i = 0; i < 4; i++) {  // Делаю поворот
                    ArrayList<AxialCoordinate> newState = new ArrayList<>(clockwise(states.get(i), x, z, y));
                    states.add(newState);
                }
            } else {
                for (int i = 0; i < 4; i++) {  // Делаю поворот
                    states.add(null);
                }
            }
        }


        private ArrayList<AxialCoordinate> clockwise(ArrayList<AxialCoordinate> state, int x, int z, int y) {
            ArrayList<AxialCoordinate> newState = new ArrayList<>();
            for (AxialCoordinate coordinate : state)
                try {
                    newState.add(coordinate.clone());
                } catch (CloneNotSupportedException e) {
                }
            for (int i = 0; i < state.size(); i++)
                newState.get(i).setCoordinate(-(state.get(i).getGridZ() - z) + x, -(-state.get(i).getGridX() - state.get(i).getGridZ() - y) + z);
            pivots.add(fromCoordinates(newState.get(0).getGridX() - x,newState.get(0).getGridZ() - z));
            return newState;
        }
    }


    private class Position {  // класс для выбранных позиций
        //TODO: добавить парамметров и расставить более полезные приоритеты
        int neighbours, depth, rows, priority, state;
        ArrayList<AxialCoordinate> coordinates;

        private Position(ArrayList<AxialCoordinate> coordinates, AxialCoordinate first, int state) {
            // первая координата ставится на координату first, а остальные переносятся за ней.
            neighbours = 0;
            rows = 0;
            depth = 0;
            this.state = state;
            this.coordinates = new ArrayList<>();
            for (AxialCoordinate coordinate : coordinates)
                try {
                    this.coordinates.add(coordinate.clone());
                } catch (CloneNotSupportedException e) {}

            int dx = first.getGridX() - this.coordinates.get(0).getGridX();
            int dz = first.getGridZ() - this.coordinates.get(0).getGridZ();
            this.coordinates.get(0).setCoordinate(first.getGridX(), first.getGridZ());

            // Скорее всего здесь более сложный перенос и стоит подумать еще
            for (int i = 1; i < this.coordinates.size(); i++)
                this.coordinates.get(i).setCoordinate(this.coordinates.get(i).getGridX() + dx, this.coordinates.get(i).getGridZ() + dz);
        }


        private void makePriority() {
            for (int i = 0; i < this.coordinates.size(); i++) {
                // Ищем, где фигура касается наиболее "глубокого" ряда
                depth += this.coordinates.get(i).getGridZ() + 5;

                // Если есть сосед справа у одного из хексов то добавляем очко
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ()) != null && lockedHexagons.get(this.coordinates.get(i).getGridZ()).contains(this.coordinates.get(i).getGridX() + 1))
                    neighbours += 1;

                // -//- для левого соседа
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ()) != null && lockedHexagons.get(this.coordinates.get(i).getGridZ()).contains(this.coordinates.get(i).getGridX() - 1))
                    neighbours += 1;

                //залоченный снизу справа
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ() + 1) != null && lockedHexagons.get(this.coordinates.get(i).getGridZ() + 1).contains(this.coordinates.get(i).getGridX()))
                    neighbours += 1;

                //снизу слева залоченный
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ() + 1) != null && lockedHexagons.get(this.coordinates.get(i).getGridZ() + 1).contains(this.coordinates.get(i).getGridX() - 1))
                    neighbours += 1;

                // края сетки снизу
                if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX(), this.coordinates.get(i).getGridZ() + 1)) || !hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX() - 1, this.coordinates.get(i).getGridZ() + 1)))
                    neighbours += 1;

                // если касается одной из стенок, то тоже считаем как соседа
                if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX() - 1, this.coordinates.get(i).getGridZ())) || !hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX() + 1, this.coordinates.get(i).getGridZ())))
                    neighbours += 1;


                ArrayList<AxialCoordinate> axialCoordinate = new ArrayList<>();
                for (AxialCoordinate coordinate : coordinates)
                    try {
                        axialCoordinate.add(coordinate.clone());
                    } catch (CloneNotSupportedException e) {
                    }

                for (int j = 0; j < axialCoordinate.size(); j++) {
                    int k = 0;
                    for (AxialCoordinate coordinate : axialCoordinate)
                        if (coordinate.getGridZ() == axialCoordinate.get(j).getGridZ())
                            k++;

                    if (lockedHexagons.get(coordinates.get(j).getGridZ()) != null && lockedHexagons.get(coordinates.get(j).getGridZ()).size() + k == hexagonalGrid.getWidth())
                        rows += hexagonalGrid.getWidth() * coordinates.get(j).getGridZ();
                }
                priority = depth + neighbours + rows;
            }
        }


        private boolean isValid() {
            boolean locked = false;
            //проверка, что ни один хекс фигуры в этой позиции не находится уже на залоченном или вне поля.
            for (AxialCoordinate coordinate : coordinates) {
                if ((lockedHexagons.get(coordinate.getGridZ()) != null && lockedHexagons.get(coordinate.getGridZ()).contains(coordinate.getGridX())) || !hexagonalGrid.containsAxialCoordinate(fromCoordinates(coordinate.getGridX(), coordinate.getGridZ()))) {
                    return false;
                }
                if ((lockedHexagons.get(coordinate.getGridZ() + 1) != null && lockedHexagons.get(coordinate.getGridZ() + 1).contains(coordinate.getGridX())) || !hexagonalGrid.containsAxialCoordinate(fromCoordinates(coordinate.getGridX(), coordinate.getGridZ() + 1)))
                    locked = true;
                if ((lockedHexagons.get(coordinate.getGridZ() + 1) != null && lockedHexagons.get(coordinate.getGridZ() + 1).contains(coordinate.getGridX() - 1)) || !hexagonalGrid.containsAxialCoordinate(fromCoordinates(coordinate.getGridX() - 1, coordinate.getGridZ() + 1)))
                    locked = true;
            }
            return locked;
        }
    }
}