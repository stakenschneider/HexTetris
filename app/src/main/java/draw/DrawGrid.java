package draw;

import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;

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


public void useBuilder(Canvas canvas)
{
    int gridWidth = 8;
    int gridHeight = 15;
    int radius = 30;

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
        drawPoly(canvas,convertToPointsArr(hexagon.getPoints(),array));
    }
}


    private void drawPoly(Canvas canvas, int[] array) {

        if (array.length < 12) {
            return;
        }

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


