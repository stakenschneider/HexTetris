package draw;

import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;

import java.util.List;
import java.util.Set;

import com.example.masha.tetris.Controller;
import api.AxialCoordinate;
import api.exception.HexagonalGridCreationException;
import api.Hexagon;
import api.HexagonOrientation;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import api.HexagonalGridCalculator;
import api.HexagonalGridLayout;
import api.Point;

import static api.HexagonOrientation.POINTY_TOP;
import static api.HexagonalGridLayout.RECTANGULAR;
import static com.example.masha.tetris.Settings.width;
import static com.example.masha.tetris.Settings.height;
import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;


public class DrawGrid {

    private static final HexagonOrientation DEFAULT_ORIENTATION = POINTY_TOP;
    private static final HexagonalGridLayout DEFAULT_GRID_LAYOUT = RECTANGULAR;
    public HexagonalGrid hexagonalGrid;
    private HexagonalGridCalculator hexagonalGridCalculator;
    private Controller controller;
    private HexagonOrientation orientation = DEFAULT_ORIENTATION;
    private HexagonalGridLayout hexagonGridLayout = DEFAULT_GRID_LAYOUT;
    double radius;
    NormalPack pack;


    public DrawGrid () {

        if (height < 15) height = 15; //эти строчки надо удалить когда разберусь с preferen и переходами между activity
        // (в жопу разбираться с preferen оставим так)
        if (width < 8) width = 8;

        radius = radGame();

        try {
            HexagonalGridBuilder builder = new HexagonalGridBuilder()
                    .setGridWidth(width)
                    .setGridHeight(height)
                    .setRadius(radius)
                    .setOrientation(orientation)
                    .setGridLayout(hexagonGridLayout);
            hexagonalGrid = builder.build();
            hexagonalGridCalculator = builder.buildCalculatorFor(hexagonalGrid);
            controller = new Controller(builder.getCustomStorage(),hexagonalGrid.getLockedHexagons());
        } catch (HexagonalGridCreationException e) {}
        pack = new NormalPack(hexagonalGrid);
    }


    public void useBuilder(Canvas canvas, String movement) {

        switch (movement) {
            case "START":
                pack.getFigure(width);
                controller.deleteRow();

                break;

            case "COUNTER_CLCK":
                controller.rotationCounterClockwise(hexagonalGrid);
                controller.deleteRow();

                break;

            case "CLCK":
                controller.rotationClockwise(hexagonalGrid);
                controller.deleteRow();

                break;

            case "DOWN_RIGHT":
                controller.moveDownRight(hexagonalGrid);
                controller.deleteRow();

                break;

            case "DOWN_LEFT":
                controller.moveDownLeft(hexagonalGrid);
                controller.deleteRow();


                break;

            case "RIGHT":
                controller.moveRight(hexagonalGrid);
                controller.deleteRow();
                break;

            case "LEFT":
                controller.moveLeft(hexagonalGrid);
                controller.deleteRow();

                break;
        }

        if (hexagonalGrid.getHexagonStorage().isEmpty())
            pack.getFigure(width);


        canvas.drawColor(Color.parseColor("#1B2024"));

        for (AxialCoordinate axialCoordinate : hexagonalGrid.getHexagonStorage()) { //фигруа
            int[] array = new int[12];
            drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(axialCoordinate).get().getPoints(), array), "#81AA21", Style.FILL);
        }

        Set <AxialCoordinate> coordinates = hexagonalGrid.getLockedHexagons().keySet();
        for (AxialCoordinate coordinate: coordinates) //залоченные фигуры
        {
            int[] array = new int[12];
            drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(coordinate).get().getPoints(), array),  "#FF5346", Style.FILL);
        }

        for (Hexagon hexagon : hexagonalGrid.getHexagons()) { //сетка
            int[] array = new int[12];
            drawPoly(canvas, convertToPointsArr(hexagon.getPoints(), array), "#FF5346", Style.STROKE);
        }

    }


    private void drawPoly(Canvas canvas, int[] array, String color,  Style style) {

        if (array.length < 12)
            return;

        Paint p = new Paint();

        p.setColor(Color.parseColor(color));
        p.setStyle(style);

        if (width > 15)
            p.setStrokeWidth(2);
        else if (width>30)p.setStrokeWidth(1);
        else p.setStrokeWidth(5);

        Path polyPath = new Path();
        polyPath.moveTo(array[0], array[1]);

        for (int i = 0; i < 12;  i=i+2 )
            polyPath.lineTo(array[i], array[i+1]);

        polyPath.lineTo(array[0], array[1]);
        canvas.drawPath(polyPath, p);

        p.setStrokeWidth(1);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setTextSize(40);
        canvas.drawText("score: "  /** + point*/, 30 , (float)scrh-15, p);

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


    public double radGame()
    {
        radius = 2*scrw/(Math.sqrt(3)*(2*width+1)); //расчитываем радиус по ширине
        int dfkj = 50;
        if ((radius*(height / 2 + height + (Math.sqrt(3) / 2 / 2))) > (scrh-dfkj) && height % 2 == 0)
            radius = (scrh-dfkj) / ( height/ 2 + height + (Math.sqrt(3) / 2 / 2));
        else if ((radius*( height + ((height+1) /2))) > (scrh-dfkj) && height % 2 != 0)
            radius = (scrh-dfkj) / ( height + ((height+1) /2));

        return radius;
    }


}