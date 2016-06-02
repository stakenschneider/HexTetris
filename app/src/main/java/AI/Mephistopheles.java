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

/**
 * Created by Timus and Masha on 01.06.16.
 */

public class Mephistopheles {

    private class ComplexFigure{   // класс для работы с фигурой для которой ищем наилучшую позицию
        ArrayList<ArrayList<AxialCoordinate>> states; //6 положений

        public ComplexFigure(ArrayList<AxialCoordinate> hexs) {
            states = new ArrayList<ArrayList<AxialCoordinate>>();
            makeStates(hexs);
        }

        private void makeStates(ArrayList<AxialCoordinate> hexs)  // создаем 6 положений фигуры
        {
            //TODO Нужно создать копию массива hexs (копия всех элементов, а не ссылок на них) и работать уже с ней
            int x = hexs.get(0).getGridX() , z = hexs.get(0).getGridZ() , y = - x - z;

            hexs.remove(0);  // убрал точку поворота, так как она больше уже не нужна

            for (int i = 0; i<hexs.size(); i++)
                hexs.get(i).setCoordinate(hexs.get(i).getGridX()-hexs.get(0).getGridX(),hexs.get(i).getGridZ()-hexs.get(0).getGridZ()); // Первую координату представил как (0,0), а остальные сдвинул относитльно этого)
            states.add(hexs);
            for (int i = 0; i<5; i++)  // Делаю поворот и также делаю сдвиг отсчета в первую координату
            {
                ArrayList<AxialCoordinate> newState = clockwise(states.get(i),x,z,y);
                int dx = newState.get(0).getGridX(), dz = newState.get(0).getGridZ();
                for (int j = 0; j<newState.size(); j++)
                    newState.get(j).setCoordinate(newState.get(j).getGridX()-dx,newState.get(j).getGridZ()-dz);
                states.add(newState);
            }
        }

        private ArrayList<AxialCoordinate> clockwise( final ArrayList<AxialCoordinate> state, int x, int z , int y){
            ArrayList<AxialCoordinate> newState = state;
            for (int i = 1; i<state.size(); i++){
                newState.get(i).setCoordinate(-(state.get(i).getGridZ() - z) + x, -(-state.get(i).getGridX() - state.get(i).getGridZ() - y) + z);
        }
            return newState;
        }
    }
    private class Position {  // класс для выбранных позиций
        int neighbours; // очки за соседей
        int depth;      // очки за глубину
        int rows;       // очки за уничтожение рядов
        int priority;
        ArrayList<AxialCoordinate> coordinates;
        private Position(ArrayList<AxialCoordinate> coordinates)
        {
            neighbours = 0;
            rows = 0;
            depth = 0;
            this.coordinates = coordinates;
            for (int i = 0; i < coordinates.size(); i++)
            {
                if (depth<=coordinates.get(i).getGridZ()) depth = coordinates.get(i).getGridZ(); // Ищем, где фигура касается наиболее "глубокого" ряда
                if (lockedHexagons.get(coordinates.get(i).getGridZ()).contains(coordinates.get(i).getGridX()+1)) // Если есть сосед справа у одного из хексов то добавляем очко
                    neighbours++;
                if (lockedHexagons.get(coordinates.get(i).getGridZ()).contains(coordinates.get(i).getGridX()-1)) // -//- для левого соседа
                    neighbours++;
                if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(coordinates.get(i).getGridX()-1,coordinates.get(i).getGridZ()))
                        ||!hexagonalGrid.containsAxialCoordinate(fromCoordinates(coordinates.get(i).getGridX()+1,coordinates.get(i).getGridZ()))) // если касается одной из стенок, то тоже считаем как соседа
                    neighbours++;
                // TODO Надо как то добавить счет уничтоженных рядов, но и не изменять lockedHexagons (может тоже стоит созлать копию и работать с ней)
                priority = depth + neighbours;
            }
        }

        /* Проверка вмещается ли фигура в позицию.
           Новая позиция рассматривается если у залоченного хекса из lockedHexagons есть свободный правый или левый сосед.
           Тогда первая координата одного из состояний фигуры (state) занимает это свободное место.
           В этом методе же проверяется занимают ли остальные хексы свободное место.
           Если вся фигура влезла, то позиция считается валидной.
         */
        private boolean isValid(int x, int z)
        {
            for (AxialCoordinate coordinate : coordinates)
            {
                if (lockedHexagons.get(z+coordinate.getGridZ()).contains(x + coordinate.getGridX())||!hexagonalGrid.containsAxialCoordinate(fromCoordinates(x+coordinate.getGridX(),z+coordinate.getGridZ())))
                    return false;
            }
            return true;
        }
    }
    public SparseArray<ArrayList<Integer>> lockedHexagons;
    public  HexagonalGrid hexagonalGrid;
    protected Comparator<Position> comparator = (Position pos1, Position pos2) -> (pos2.priority - pos1.priority); // Компаратор для сортировки позиций по их приоритету (по убыванию)
    private Queue<Position> positions; // позиции размещенные по убыванию
    HexagonalGridCalculator calculator;
    ArrayList<AxialCoordinate> hexs; // начальные координаты фигуры
    LinkedList<String> path; // Возвращаемый путь для Controller


    public Mephistopheles(ArrayList<AxialCoordinate> hexs, HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator) {
        this.hexs = hexs;
        ComplexFigure figure = new ComplexFigure(hexs);
        this.hexagonalGrid = hexagonalGrid;
        this.lockedHexagons = hexagonalGrid.getLockedHexagons();
        positions =  new PriorityQueue<>(20, comparator);
        this.calculator = calculator;
    }

     public LinkedList<String> startSearch(ComplexFigure figure)
     {
         for (int i =0; i<hexagonalGrid.getHeight(); i++) // Рассматриваем последовательно каждый ряд
         {
              for (Integer x : lockedHexagons.get(i)) // Выбираем залоченные хексы и смотрим есть ли у них пустые соседи
              {
                  int x1 = x + 1;
                  int x2 = x - 1;
                  if (!lockedHexagons.get(i).contains(x1)) // проверка правого соседа
                  {
                      for (ArrayList<AxialCoordinate> state : figure.states) // для каждого положения фигуры проверяем валидность позиции
                      {
                          Position position = new Position(state);
                          if (position.isValid(x1,i))
                              positions.add(position);
                      }
                  }
                  if (!lockedHexagons.get(i).contains(x2)) // проверка левого соседа
                  {
                      for (ArrayList<AxialCoordinate> state : figure.states)
                      {
                          Position position = new Position(state);
                          if (position.isValid(x2,i))
                              positions.add(position);
                      }
                  }
              }
         }





         // Также на всякий случай берем самый нижний полностью незаполненный ряд и пытаемся пристроить фигуру к стенке
         int row = 0;
         while (lockedHexagons.get(row+1)==null)
         {
             row++;
             if (row == hexagonalGrid.getHeight()) break;
         }
         // Пока что есть только для левой стенки
         for (ArrayList<AxialCoordinate> state : figure.states)
         {
             Position position = new Position(state);
             if (position.isValid(0-row/2,row))
                 positions.add(position);
         }


         // Так как позиции расположены с самой лучшей, то берем первую и пытаемся проложить путь, если не получилось, то берем следующую и повторяем
         while (path == null) {
             Pathfinding pathfinding = new Pathfinding(hexagonalGrid, calculator, hexs, positions.poll().coordinates, hexs.get(0)); //positions.poll() возвращает первую по приоритету позицию и сразу удаляет ее из очереди
             path = pathfinding.findPath();
         }

         return path;
     }




}
