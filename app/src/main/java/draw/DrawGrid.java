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

import java.util.List;

import api.Hexagon;
import api.HexagonOrientation;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import api.HexagonalGridCalculator;
import api.HexagonalGridLayout;
import api.Point;

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

    double radius; //описаный
    int gridWidth = width;
    int gridHeight = height;



    public void useBuilder(Canvas canvas , int act) //
    {

        if (gridHeight == 0) gridHeight = 15; //эти строчки надо удалить когда разберусь с preferen и переходами между activity
        if (gridWidth == 0) gridWidth = 8;
        radius = radGame();

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
            drawPoly(canvas,convertToPointsArr(hexagon.getPoints(),array) , act);
        }
    }


    private void drawPoly(Canvas canvas, int[] array , int act) {

        if (array.length < 12)
            return;

        Paint p = new Paint();
        p.setColor(Color.rgb(250, 175, 6));
        p.setStyle(Style.STROKE);
        p.setStrokeWidth(5);

        Path polyPath = new Path();
        polyPath.moveTo(array[0], array[1]); //первая точка
        int i;

        for (i = 0; i < 12;  i=i+2 )
            polyPath.lineTo(array[i], array[i+1]);

        polyPath.lineTo(array[0], array[1]);

        canvas.drawPath(polyPath, p);

        p.setStrokeWidth(1);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setTextSize(40);

        if (act == 0)
        canvas.drawText("score:" , 30 , (float)scrh-15, p);
        else
        {
            canvas.drawText("add" , 30 , (float)scrh-15, p);
            canvas.drawText("play" , (float)scrw/2 , (float)scrh-15, p);
        }
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


    public double radGame()
    {
        radius = 2*scrw/(Math.sqrt(3)*(2*gridWidth+1)); //расчитываем радиус по ширине

        int dfkj = 50; //отступ для score или
        if ((radius*(gridHeight / 2 + gridHeight + (Math.sqrt(3) / 2 / 2))) > (scrh-dfkj) && gridHeight % 2 == 0)  // если в итоге он больше а колво в высоту четное
            radius = (scrh-dfkj) / (gridHeight / 2 + gridHeight + (Math.sqrt(3) / 2 / 2)); //выравнивание по высоте для четного
        else if ((radius*( gridHeight + ((gridHeight+1) /2))) > (scrh-dfkj) && gridHeight % 2 != 0) //если больше и кол во нч
            radius = (scrh-dfkj) / ( gridHeight + ((gridHeight+1) /2));  //выравнивание по высоте для нч

        return radius;
    }


}