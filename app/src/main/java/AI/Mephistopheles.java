package AI;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import api.AxialCoordinate;
import api.HexagonalGrid;
import api.HexagonalGridCalculator;

import static api.AxialCoordinate.fromCoordinates;


public class Mephistopheles {

    public SparseArray<ArrayList<Integer>> lockedHexagons;
    public  HexagonalGrid hexagonalGrid;
    protected Comparator<Position> comparator = (Position pos1, Position pos2) -> (pos2.priority - pos1.priority); // Компаратор для сортировки позиций по их приоритету (по убыванию)
    private Queue<Position> positions; // позиции размещенные по убыванию
    HexagonalGridCalculator calculator;
    LinkedList<String> path;           // Возвращаемый путь для Controller


    public Mephistopheles(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator) {
        this.hexagonalGrid = hexagonalGrid;
        this.lockedHexagons = hexagonalGrid.getLockedHexagons();
        positions =  new PriorityQueue<>(20, comparator);
        this.calculator = calculator;
    }

    public LinkedList<String> startSearch(ArrayList<AxialCoordinate> hexs) {
        ArrayList<AxialCoordinate> start = new ArrayList<>();
        for (AxialCoordinate coordinate:hexs)
            // TODO Проверить, действительно ли здесь происходит копирование элементов, а не ссылок. Если это не так, то все очень плохо
            start.add(fromCoordinates(coordinate.getGridX(),coordinate.getGridZ()));  // Нужен копия массива поступивших хексов, но не ссылка на поступивший. (значения в массиве hexs меняться не должны)

//        ComplexFigure figure = new ComplexFigure(start);

        //Здесь должен быть сам цикл поиска лучшей фигуры

        // Так как позиции расположены с самой лучшей, то берем первую и пытаемся проложить путь, если не получилось, то берем следующую и повторяем
        AxialCoordinate pivot = fromCoordinates(start.get(0).getGridX(),start.get(0).getGridZ());
        start.remove(0);
        while (path == null) {
            //positions.poll() возвращает первую по приоритету позицию и сразу удаляет ее из очереди
            Pathfinding pathfinding = new Pathfinding(hexagonalGrid, calculator, start, positions.poll().coordinates, pivot);
            path = pathfinding.findPath();
        }
        return path;
    }



    private class ComplexFigure{
        ArrayList<ArrayList<AxialCoordinate>> states; //6 положений

        public ComplexFigure(ArrayList<AxialCoordinate> hexs) {
            states = new ArrayList<>();
            makeStates(hexs);
        }

        private void makeStates(ArrayList<AxialCoordinate> hexs){  // создаем 6 положений фигуры
            ArrayList<AxialCoordinate> firstState = new ArrayList<>();
            for (AxialCoordinate coordinate:hexs)
                firstState.add(fromCoordinates(coordinate.getGridX(), coordinate.getGridZ())); //Скопировали массив

            //Взяли координаты точки поворота
            int x = firstState.get(0).getGridX(), z = firstState.get(0).getGridZ()  , y = - x - z;

            firstState.remove(0);  // убрал точку поворота, так как она больше уже не нужна
            states.add(firstState);
            for (int i = 0; i<4; i++){  // Делаю поворот
                ArrayList<AxialCoordinate> newState = new ArrayList<>(clockwise(states.get(i), x, z, y));
                states.add(newState);
            }
        }

        //Поворот
        private ArrayList<AxialCoordinate> clockwise(ArrayList<AxialCoordinate> state, int x, int z, int y){
            ArrayList<AxialCoordinate> newState = state;
            for (int i = 0; i < state.size(); i++)
                newState.get(i).setCoordinate(-(state.get(i).getGridZ() - z) + x, -(-state.get(i).getGridX() - state.get(i).getGridZ() - y) + z);

            return newState;
        }
    }


    private class Position {  // класс для выбранных позиций
        int neighbours; // очки за соседей
        int depth;      // очки за глубину
        int rows;       // очки за уничтожение рядов
        int priority;
        ArrayList<AxialCoordinate> coordinates;
        private Position(ArrayList<AxialCoordinate> coordinates, AxialCoordinate first) {
            // первая координата ставится на координату first, а остальные переносятся за ней.
            neighbours = 0;
            rows = 0;
            depth = 0;
            this.coordinates = new ArrayList<>();
            for (AxialCoordinate coordinate : coordinates)
                this.coordinates.add(fromCoordinates(coordinate.getGridX(),coordinate.getGridZ()));

            int dx = first.getGridX() - this.coordinates.get(0).getGridX();
            int dz = first.getGridZ() - this.coordinates.get(0).getGridZ();
            this.coordinates.get(0).setCoordinate(first.getGridX(),first.getGridZ());

            // Скорее всего здесь более сложный перенос и стоит подумать еще
            for (int i = 1; i< this.coordinates.size(); i++)
                this.coordinates.get(i).setCoordinate( this.coordinates.get(i).getGridX() + dx, this.coordinates.get(i).getGridZ()+ dz);
        }


        private int makePriority() {
            for (int i = 0; i < this.coordinates.size(); i++) {
                // Ищем, где фигура касается наиболее "глубокого" ряда
                if (depth <= this.coordinates.get(i).getGridZ()) depth = this.coordinates.get(i).getGridZ();

                // Если есть сосед справа у одного из хексов то добавляем очко
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ()) != null&&lockedHexagons.get(this.coordinates.get(i).getGridZ()).contains(this.coordinates.get(i).getGridX() + 1))
                    neighbours++;

                // -//- для левого соседа
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ()) != null&&lockedHexagons.get(this.coordinates.get(i).getGridZ()).contains(this.coordinates.get(i).getGridX() - 1))
                    neighbours++;

                // если касается одной из стенок, то тоже считаем как соседа
                if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX()-1,this.coordinates.get(i).getGridZ())) || !hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX()+1,this.coordinates.get(i).getGridZ())))
                    neighbours++;
                // TODO Надо как то добавить счет уничтоженных рядов, но и не изменять lockedHexagons (может тоже стоит созлать копию и работать с ней)
                priority = depth + neighbours +rows;
            }
            return priority;
        }


         private boolean isValid(){
            for (AxialCoordinate coordinate : coordinates){
                // TODO Сделать проверку того, что фигура залочится при этой позиции
                //проверка, что ни один хекс фигуры в этой позиции не находится уже на залоченном или вне поля.
                if (lockedHexagons.get(coordinate.getGridZ()) != null&&(lockedHexagons.get(coordinate.getGridZ()).contains(coordinate.getGridX()) || !hexagonalGrid.containsAxialCoordinate(fromCoordinates(coordinate.getGridX(), coordinate.getGridZ()))))
                    return false;
            }
            return true;
        }
    }
}