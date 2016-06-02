package AI;

import android.util.Log;
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
            ArrayList<AxialCoordinate> firstState = new ArrayList<AxialCoordinate>();
            for (AxialCoordinate coordinate:hexs)
            {
                firstState.add(fromCoordinates(coordinate.getGridX(), coordinate.getGridZ()));
            }
            int x = firstState.get(0).getGridX(), z = firstState.get(0).getGridZ()  , y = - x - z;

            firstState.remove(0);  // убрал точку поворота, так как она больше уже не нужна
            //
            //for (int i = 0; i<firstState.size(); i++)
            //   firstState.get(i).setCoordinate(hexs.get(i).getGridX()-firstState.get(0).getGridX(),firstState.get(i).getGridZ()-firstState.get(0).getGridZ()); // Первую координату представил как (0,0), а остальные сдвинул относитльно этого)
            states.add(firstState);
            for (int i = 0; i<4; i++)  // Делаю поворот и также делаю сдвиг отсчета в первую координату
            {
                ArrayList<AxialCoordinate> newState = clockwise(states.get(i), x, z, y);
                states.add(newState);
            }
            for (int i = 0; i<5; i++)
            {
               int dx = states.get(i).get(0).getGridX();
               int dz = states.get(i).get(0).getGridZ();
                for (int j = 0; j<states.get(i).size(); j++)
                    states.get(i).get(j).setCoordinate(states.get(i).get(j).getGridX()-dx,states.get(i).get(j).getGridZ()-dz);
            }
        }

        private ArrayList<AxialCoordinate> clockwise(ArrayList<AxialCoordinate> state, int x, int z , int y){
            ArrayList<AxialCoordinate> newState = state;
            for (int i = 0; i<state.size(); i++){
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
        private Position(ArrayList<AxialCoordinate> coordinates, AxialCoordinate first)
        {
            neighbours = 0;
            rows = 0;
            depth = 0;
            this.coordinates = new ArrayList<AxialCoordinate>();
            for (AxialCoordinate coordinate:coordinates)
            {
                this.coordinates.add(fromCoordinates(coordinate.getGridX(),coordinate.getGridZ()));
            }
            for (int i = 0; i< this.coordinates.size(); i++)
            {
                this.coordinates.get(i).setCoordinate(0 + first.getGridX(), 0 + first.getGridZ());
            }
            for (int i = 0; i < coordinates.size(); i++)
            {
                if (depth<=this.coordinates.get(i).getGridZ()) depth = this.coordinates.get(i).getGridZ(); // Ищем, где фигура касается наиболее "глубокого" ряда
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ())!=null&&lockedHexagons.get(this.coordinates.get(i).getGridZ()).contains(this.coordinates.get(i).getGridX()+1)) // Если есть сосед справа у одного из хексов то добавляем очко
                    neighbours++;
                if (lockedHexagons.get(this.coordinates.get(i).getGridZ())!=null&&lockedHexagons.get(this.coordinates.get(i).getGridZ()).contains(coordinates.get(i).getGridX()-1)) // -//- для левого соседа
                    neighbours++;
                if (!hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX()-1,this.coordinates.get(i).getGridZ()))
                        ||!hexagonalGrid.containsAxialCoordinate(fromCoordinates(this.coordinates.get(i).getGridX()+1,this.coordinates.get(i).getGridZ()))) // если касается одной из стенок, то тоже считаем как соседа
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
                // TODO Сделать проверку того, что фигура залочится при этой позиции
                if (lockedHexagons.get(z + coordinate.getGridZ()) != null&&(lockedHexagons.get(z+coordinate.getGridZ()).contains(x + coordinate.getGridX())||!hexagonalGrid.containsAxialCoordinate(fromCoordinates(x+coordinate.getGridX(),z+coordinate.getGridZ()))))
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
    LinkedList<String> path; // Возвращаемый путь для Controller


    public Mephistopheles(HexagonalGrid hexagonalGrid, HexagonalGridCalculator calculator) {
        this.hexagonalGrid = hexagonalGrid;
        this.lockedHexagons = hexagonalGrid.getLockedHexagons();
        positions =  new PriorityQueue<Position>(20, comparator);
        this.calculator = calculator;
    }

     public LinkedList<String> startSearch(ArrayList<AxialCoordinate> hexs)
     {
         ArrayList<AxialCoordinate> start = new ArrayList<AxialCoordinate>();
         for (AxialCoordinate coordinate:hexs)
         {
             start.add(fromCoordinates(coordinate.getGridX(),coordinate.getGridZ()));
         }

         ComplexFigure figure = new ComplexFigure(start);
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
                          Position position = new Position(state, fromCoordinates(x1,i));
                          if (position.isValid(x1,i))
                              positions.add(position);
                      }
                  }
                  if (!lockedHexagons.get(i).contains(x2)) // проверка левого соседа
                  {
                      for (ArrayList<AxialCoordinate> state : figure.states)
                      {
                          Position position = new Position(state, fromCoordinates(x2, i));
                          if (position.isValid(x2,i))
                              positions.add(position);
                      }
                  }
              }
         }





         /** Также на всякий случай берем самый нижний полностью незаполненный ряд и пытаемся пристроить фигуру к стенке
         int row = 0;
         while (lockedHexagons.get(row+1)==null)
         {
             row++;
             if (row == hexagonalGrid.getHeight()) break;
         }
         // Пока что есть только для левой стенки
         for (ArrayList<AxialCoordinate> state : figure.states)
         {
             Position position = new Position(state, fromCoordinates(0-row/2,row));
             if (position.isValid(0-row/2,row))
                 positions.add(position);
         }
          */


         // Так как позиции расположены с самой лучшей, то берем первую и пытаемся проложить путь, если не получилось, то берем следующую и повторяем
         AxialCoordinate pivot = start.get(0);
         start.remove(0);
         while (path == null) {
             Pathfinding pathfinding = new Pathfinding(hexagonalGrid, calculator, start, positions.poll().coordinates, pivot); //positions.poll() возвращает первую по приоритету позицию и сразу удаляет ее из очереди
             path = pathfinding.findPath();
         }

         return path;
     }




}
