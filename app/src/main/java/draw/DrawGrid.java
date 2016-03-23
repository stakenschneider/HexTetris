package draw;

import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;

import static com.example.masha.tetris.Settings.width;
import static com.example.masha.tetris.Settings.height;
import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;

import java.util.ArrayList;
import java.util.List;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonOrientation;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import api.HexagonalGridCalculator;
import api.HexagonalGridLayout;
import api.Point;
import com.example.masha.tetris.Figure;

import api.exception.HexagonalGridCreationException;
import static api.HexagonOrientation.POINTY_TOP;
import static api.HexagonalGridLayout.RECTANGULAR;


public class DrawGrid {

    private static final HexagonOrientation DEFAULT_ORIENTATION = POINTY_TOP;
    private static final HexagonalGridLayout DEFAULT_GRID_LAYOUT = RECTANGULAR;
    private HexagonalGrid hexagonalGrid;
    private HexagonalGridCalculator hexagonalGridCalculator;
    private static final String TAG = "myLogs";

    private HexagonOrientation orientation = DEFAULT_ORIENTATION;
    private HexagonalGridLayout hexagonGridLayout = DEFAULT_GRID_LAYOUT;


    public void useBuilder(Canvas canvas)
    {
        double radius; //описанный
        int gridWidth = 15;
        int gridHeight = 29;
        if ((width>gridWidth)&(height>gridHeight))
        {
             gridWidth = width;
             gridHeight = height;
        }
      radius = 2*scrw/(Math.sqrt(3)*(2*gridWidth+1)); //расчитываем радиус по ширине

        if ((radius*(gridHeight / 2 + gridHeight + (Math.sqrt(3) / 2 / 2))) > scrh && gridHeight % 2 == 0)  // если в итоге он больше а колво в высоту четное
                radius = scrh / (gridHeight / 2 + gridHeight + (Math.sqrt(3) / 2 / 2)); //выравнивание по высоте для четного
            else if ((radius*( gridHeight + ((gridHeight+1) /2))) > scrh && gridHeight % 2 != 0) //если больше и кол во нч
        radius = scrh / ( gridHeight + ((gridHeight+1) /2));  //выравнивание по высоте для нч

        try {
            HexagonalGridBuilder builder = new HexagonalGridBuilder()
                    .setGridWidth(gridWidth)
                    .setGridHeight(gridHeight)
                    .setRadius(radius)
                    .setOrientation(orientation)
                    .setGridLayout(hexagonGridLayout);
            hexagonalGrid = builder.build();
            hexagonalGridCalculator = builder.buildCalculatorFor(hexagonalGrid);
        } catch (HexagonalGridCreationException e) {}


        for (Hexagon hexagon : hexagonalGrid.getHexagons()) {
            int[] array = new int[12];
            drawPoly(canvas, convertToPointsArr(hexagon.getPoints(), array),250,6);
        }

        // Тип создал свою временную фигуру
        AxialCoordinate ax = new AxialCoordinate(2,12);
        AxialCoordinate ax1 = new AxialCoordinate(8,4);
        AxialCoordinate ax2 = new AxialCoordinate(6,6);
        AxialCoordinate ax3 = new AxialCoordinate(5,10);
        ArrayList<Hexagon> hex = new ArrayList();
        hex.add(hexagonalGrid.getByAxialCoordinate(ax).get());
        hex.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        hex.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        hex.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        Figure figure = new Figure(hex);


        int[] array0 = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(ax).get().getPoints(), array0),0, 0);
        int[] array01 = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(ax1).get().getPoints(), array01), 0, 250);
        int[] array02 = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(ax2).get().getPoints(), array02), 0, 250);
        int[] array03 = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(ax3).get().getPoints(), array03), 0, 250);



        Hexagon hexagon = hexagonalGrid.getByAxialCoordinate(figure.convertToGrid(gridWidth)).get();
        int[] array = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagon.getPoints(), array),0, 0);

        Hexagon hexagon1 = hexagonalGrid.getByAxialCoordinate(figure.getNewCoordinate(hex.get(1))).get();
        int[] array1 = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagon1.getPoints(), array1), 0, 250);

        Hexagon hexagon2 = hexagonalGrid.getByAxialCoordinate(figure.getNewCoordinate(hex.get(2))).get();
        int[] array2 = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagon2.getPoints(), array2),0, 250);

        Hexagon hexagon3 = hexagonalGrid.getByAxialCoordinate(figure.getNewCoordinate(hex.get(3))).get();
        int[] array3 = new int[12];
        drawPoly(canvas, convertToPointsArr(hexagon3.getPoints(), array3),0, 250);

    }



    private void drawPoly(Canvas canvas, int[] array, int color, int color1) {

        if (array.length < 12) {
            return;
        }

        Paint p = new Paint();
        p.setColor(Color.rgb(color, 175, color1));
        p.setStyle(Style.STROKE);
        p.setStrokeWidth(5);

        Path polyPath = new Path();
        polyPath.moveTo(array[0], array[1]); //первая точка
        int i;

        for (i = 0; i < 12;  i=i+2 )
            polyPath.lineTo(array[i], array[i+1]);

        polyPath.lineTo(array[0], array[1]);

        canvas.drawPath(polyPath, p);
    }


    private int[] convertToPointsArr(List<Point> points,int[] array) {
        int idx = 0;
        for (Point point : points) {
            array[idx] = (int) Math.round(point.getCoordinateX());
            array[idx+1] = (int) Math.round(point.getCoordinateY());
            idx=idx+2;
        }
        return array;
    }
}