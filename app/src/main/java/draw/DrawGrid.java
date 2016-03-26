package draw;

import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.util.Log;

import static com.example.masha.tetris.Settings.width;
import static com.example.masha.tetris.Settings.height;
import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;

import java.util.List;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonOrientation;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import api.HexagonalGridCalculator;
import api.HexagonalGridLayout;
import api.Point;

import api.exception.HexagonalGridCreationException;
import internal.impl.HexagonData;

import static api.HexagonOrientation.POINTY_TOP;
import static api.HexagonalGridLayout.RECTANGULAR;


import com.example.masha.tetris.Controller;
import com.example.masha.tetris.Figure;


import java.util.ArrayList;



public class DrawGrid {

    private static final HexagonOrientation DEFAULT_ORIENTATION = POINTY_TOP;
    private static final HexagonalGridLayout DEFAULT_GRID_LAYOUT = RECTANGULAR;
    private HexagonalGrid hexagonalGrid;
    private HexagonalGridCalculator hexagonalGridCalculator;
    private static final String TAG = "myLogs";
    private Controller controller;
    private HexagonOrientation orientation = DEFAULT_ORIENTATION;
    private HexagonalGridLayout hexagonGridLayout = DEFAULT_GRID_LAYOUT;
    private int gridWidth = width;
    private int gridHeight = height;

    public DrawGrid () {

        double radius =  2 * scrw / (Math.sqrt(3) * (2 * gridWidth + 1)); //расчитываем радиус по ширине


        if ((radius * (gridHeight / 2 + gridHeight + (Math.sqrt(3) / 2 / 2))) > scrh && gridHeight % 2 == 0)  // если в итоге он больше а колво в высоту четное
            radius = scrh / (gridHeight / 2 + gridHeight + (Math.sqrt(3) / 2 / 2)); //выравнивание по высоте для четного
        else if ((radius * (gridHeight + ((gridHeight + 1) / 2))) > scrh && gridHeight % 2 != 0) //если больше и кол во нч
            radius = scrh / (gridHeight + ((gridHeight + 1) / 2));  //выравнивание по высоте для нч

        try {
            HexagonalGridBuilder builder = new HexagonalGridBuilder()
                    .setGridWidth(gridWidth)
                    .setGridHeight(gridHeight)
                    .setRadius(radius)
                    .setOrientation(orientation)
                    .setGridLayout(hexagonGridLayout);
            hexagonalGrid = builder.build();
            hexagonalGridCalculator = builder.buildCalculatorFor(hexagonalGrid);
            controller = new Controller(builder.getCustomStorage());
        } catch (HexagonalGridCreationException e) {
        }
        AxialCoordinate ax = new AxialCoordinate(1, 1);
        AxialCoordinate ax1 = new AxialCoordinate(1, 2);
        AxialCoordinate ax2 = new AxialCoordinate(1, 3);
        AxialCoordinate ax3 = new AxialCoordinate(1, 4);
        ArrayList<Hexagon> hex = new ArrayList();
        hex.add(hexagonalGrid.getByAxialCoordinate(ax).get());
        hex.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        hex.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        hex.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        Figure figure = new Figure(hex);
        hexagonalGrid.getByAxialCoordinate(figure.convertToGrid(gridWidth)).get().setState(true, false);
        hexagonalGrid.getByAxialCoordinate(figure.getNewCoordinate(hex.get(1))).get().setState(true, false);
        hexagonalGrid.getByAxialCoordinate(figure.getNewCoordinate(hex.get(2))).get().setState(true, false);
        hexagonalGrid.getByAxialCoordinate(figure.getNewCoordinate(hex.get(3))).get().setState(true, false);
    }


    public void useBuilder(Canvas canvas, String movement) {
     switch (movement) {
         case "START":
             break;
         case "DOWN":
             hexagonalGrid.setHexagonStorage(controller.movedown());
             break;
         case "RIGHT":
             hexagonalGrid.setHexagonStorage(controller.moveright());
             break;
         case "LEFT":
             hexagonalGrid.setHexagonStorage(controller.moveleft());
             break;
     }
        for (Hexagon hexagon : hexagonalGrid.getHexagons()) {
            int[] array = new int[12];
            drawPoly(canvas, convertToPointsArr(hexagon.getPoints(), array), 250, 6, Style.STROKE);
        }
        for (HexagonData hexagon : hexagonalGrid.getHexagonStorage()) {
            int[] array = new int[12];
            if ((hexagonalGrid.getByAxialCoordinate(hexagon.coordinate).isPresent())&(hexagon.partOfLocked!=true)) {
                drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(hexagon.coordinate).get().getPoints(), array), 0, 250, Style.FILL_AND_STROKE);
                Log.d("V", Integer.toString(hexagon.coordinate.getGridZ() % 2));
                Log.d("D", Integer.toString(hexagon.coordinate.getGridZ()));
                Log.d("PIska", Integer.toString(hexagon.X));
                if ((hexagon.coordinate.getGridZ()%2==1)) {
                    if ((hexagon.coordinate.getGridZ() == height - 1) || (hexagon.X == width-1  ) || (hexagon.X == 0)) {
                        Log.d("D", "Peppa");
                        for (HexagonData data : hexagonalGrid.getHexagonStorage()) {

                            data.partOfLocked = true;
                        }
                    }
                }
                else { if ((hexagon.coordinate.getGridZ() == height - 1) || (hexagon.X == width -3 ) || (hexagon.X == -1)) {
                    Log.d("D", "George");
                    for (HexagonData data : hexagonalGrid.getHexagonStorage()) {
                        data.partOfLocked = true;
                    }
                }}

            }
        }
        for (HexagonData hexagon : hexagonalGrid.getHexagonStorage()) {
            int[] array = new int[12];
            if ((hexagonalGrid.getByAxialCoordinate(hexagon.coordinate).isPresent()) & (hexagon.partOfLocked == true)) {
                drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(hexagon.coordinate).get().getPoints(), array), 0, 0, Style.FILL_AND_STROKE);
            }
        }
    }



        private void drawPoly(Canvas canvas, int[] array, int color, int color1, Style style) {


            if (array.length < 12) {
                return;
            }

            Paint p = new Paint();

            p.setColor(Color.rgb(color, 175, color1));
            p.setStyle(style);
            p.setStrokeWidth(5);
            Path polyPath = new Path();
            polyPath.moveTo(array[0], array[1]); //первая точка
            int i;

            for (i = 0; i < 12;  i=i+2 )
                polyPath.lineTo(array[i], array[i+1]);

            polyPath.lineTo(array[0], array[1]);

            canvas.drawPath(polyPath, p);
        }


        private int[] convertToPointsArr (List <Point> points, int[] array) {
            int idx = 0;
            for (Point point : points) {
                array[idx] = (int) Math.round(point.getCoordinateX());
                array[idx+1] = (int) Math.round(point.getCoordinateY());
                idx=idx+2;
            }
            return array;
        }


    }