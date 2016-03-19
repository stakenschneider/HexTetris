package com.example.masha.tetris;

import android.app.Activity;

import static api.HexagonOrientation.POINTY_TOP;
import static api.HexagonalGridLayout.RECTANGULAR;

import android.os.Bundle;

import java.util.List;
import java.util.NoSuchElementException;

import api.Hexagon;
import api.HexagonOrientation;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import api.HexagonalGridCalculator;
import api.HexagonalGridLayout;
import api.Point;
import api.exception.HexagonalGridCreationException;
import backport.Optional;

import android.util.Log;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class GamePlay extends Activity {

    private static final int DEFAULT_GRID_WIDTH = 5;
    private static final int DEFAULT_GRID_HEIGHT = 5;
    private static final int DEFAULT_RADIUS = 10;
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
    private static final String Guten_TAG = "myLogs from GamePlay";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));




    }




    class DrawView extends View{   //класс с отрисовкой
        Paint p;

        public DrawView(Context context){
            super(context);
            p = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            canvas.drawRGB(11, 25, 25);  //фон
            p.setColor(Color.YELLOW);     //цвет линии
            p.setStrokeWidth(5);         //толщина линии



            for (Hexagon hexagon : hexagonalGrid.getHexagons()) {
           //     Optional<SatelliteDataImpl> data = hexagon.<SatelliteDataImpl>getSatelliteData(); // в Дополнительной информации содержится нажимали ли на этот хекс или нет
                drawEmptyHexagon(hexagon, canvas, p); // иначе нарисовать пустой хекс
            }


            regenerateHexagonGrid(canvas);
            invalidate();//постоянная перерисовка
        }

    };




    // рисуем пустой хекс
    private void drawEmptyHexagon(Hexagon hexagon , Canvas canvas , Paint p) {

     //   gc.drawPolygon(convertToPointsArr(hexagon.getPoints()));
        canvas.drawLines(convertToPointsArr(hexagon.getPoints()) , p); //аналог вышезакомченной функции
                                                                       //должна рисовать 6 линий по координатам из  convertToPointsArr

    }



    // Кладем 12 координат (x,y) для 6 точек в массив
    private float[] convertToPointsArr(List<Point> points) {
        float[] pointsArr = new float[12];
        int idx = 0;
        for (Point point : points) {
            pointsArr[idx] = (int) Math.round(point.getCoordinateX());
            pointsArr[idx + 1] = (int) Math.round(point.getCoordinateY());
            idx += 2;
        }
        return pointsArr;
    }

    // Создание сетки
    private void regenerateHexagonGrid(Canvas canvas) {

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

    }




    }










