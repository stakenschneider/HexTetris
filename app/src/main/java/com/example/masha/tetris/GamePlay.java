package com.example.masha.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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


public class GamePlay extends AppCompatActivity {

    private static final int BLUE = -16776961;
    private static final int DEFAULT_GRID_WIDTH = 20;
    private static final int DEFAULT_GRID_HEIGHT =20;
    private static final int DEFAULT_RADIUS = 20;
    private static final HexagonOrientation DEFAULT_ORIENTATION = POINTY_TOP;
    private static final HexagonalGridLayout DEFAULT_GRID_LAYOUT = RECTANGULAR;
    private static final int CANVAS_WIDTH = 1000;
    private HexagonalGrid hexagonalGrid;
    private HexagonalGridCalculator hexagonalGridCalculator;
    private int gridWidth = DEFAULT_GRID_WIDTH;
    private int gridHeight = DEFAULT_GRID_HEIGHT;
    private int radius = DEFAULT_RADIUS;
    private HexagonOrientation orientation = DEFAULT_ORIENTATION;
    private HexagonalGridLayout hexagonGridLayout = DEFAULT_GRID_LAYOUT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new CanvasView(this));
    }


    class CanvasView extends View {


        public CanvasView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(11, 25, 25);
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
    }

    private void drawPoly(Canvas canvas, int[] array) {

        if (array.length < 12) {
            return;
        }

        Paint polyPaint = new Paint();
        polyPaint.setColor(Color.YELLOW);
        polyPaint.setStyle(Style.FILL);

        Path polyPath = new Path();
        polyPath.moveTo(array[0], array[1]);
        int i, len;

        len = array.length;
        for (i = 0; i < 12; ) {

            polyPath.lineTo(array[i], array[i+1]);
            i=i+2;
        }
        polyPath.lineTo(array[0], array[1]);

        // draw
        canvas.drawPath(polyPath, polyPaint);
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






