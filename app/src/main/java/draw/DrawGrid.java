package draw;

import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.util.Log;

import JSON.InitGame;
import wrrrrrm.Controller;
import java.util.ArrayList;
import java.util.List;
import api.AxialCoordinate;
import api.exception.HexagonalGridCreationException;
import api.Hexagon;
import api.HexagonOrientation;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import api.HexagonalGridCalculator;
import api.HexagonalGridLayout;
import api.Point;
import backport.Optional;
import wrrrrrm.HeapFigure;

import static android.content.Context.LAUNCHER_APPS_SERVICE;
import static api.HexagonOrientation.POINTY_TOP;
import static api.HexagonalGridLayout.RECTANGULAR;
import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;
import static api.AxialCoordinate.fromCoordinates;
import static com.example.masha.tetris.Settings.height;
import static com.example.masha.tetris.Settings.width;



public class DrawGrid {

    private static final HexagonOrientation DEFAULT_ORIENTATION = POINTY_TOP;
    private static final HexagonalGridLayout DEFAULT_GRID_LAYOUT = RECTANGULAR;
    public HexagonalGrid hexagonalGrid;
    private HexagonalGridCalculator hexagonalGridCalculator;
    private Controller controller;
    private HexagonOrientation orientation = DEFAULT_ORIENTATION;
    private HexagonalGridLayout hexagonGridLayout = DEFAULT_GRID_LAYOUT;
    public static int point ;
    double radius;
    HeapFigure heapFigure;
    int gWidth = 0 , gHeight = 0;

    public DrawGrid (String strJSON , String game) {

        InitGame initGame = new InitGame(strJSON);

        //убого конечно но время экономит
        if (game.equals("Mephistopheles")){
            gHeight = initGame.height;
            gWidth = initGame.width;
        } else if (game.equals("GamePlay"))
        {
            gHeight = height;
            gWidth = width;
            if (height < 15) gHeight = initGame.height;
            if (width < 8) gWidth = initGame.width;
        }

        radius = rad(gWidth, gHeight);
        point = 0;

        try {
            HexagonalGridBuilder builder = new HexagonalGridBuilder()
                    .setGridWidth(gWidth)
                    .setGridHeight(gHeight)
                    .setRadius(radius)
                    .setOrientation(orientation)
                    .setGridLayout(hexagonGridLayout);
            hexagonalGrid = builder.build();
            hexagonalGridCalculator = builder.buildCalculatorFor(hexagonalGrid);
            controller = new Controller(builder.getCustomStorage(),hexagonalGrid.getLockedHexagons(), point);
        } catch (HexagonalGridCreationException e) {}

        heapFigure = new HeapFigure(hexagonalGrid , initGame.quantityUnit , strJSON);

    }


    public boolean useBuilder(Canvas canvas, String movement) {
        int[] array = new int[12];

            switch (movement) {

                case "COUNTER_CLCK":
                    controller.rotationCounterClockwise(hexagonalGrid);
                    break;

                case "CLCK":
                    controller.rotationClockwise(hexagonalGrid);
                    break;

                case "DOWN_RIGHT":
                    point = controller.moveDownRight(hexagonalGrid);
                    break;

                case "DOWN_LEFT":
                    point = controller.moveDownLeft(hexagonalGrid);
                    break;

                case "RIGHT":
                    controller.moveRight(hexagonalGrid);
                    break;

                case "LEFT":
                    controller.moveLeft(hexagonalGrid);
                    break;

                case "GAME":
                    break;

                case "START":
                    hexagonalGrid.getHexagons().forEach((Hexagon hexagon) ->
                            drawPoly(canvas, convertToPointsArr(hexagon.getPoints(), array), "#FF5346", Style.STROKE));
                    return false;

                case "LOCKED":
                    for (int z = 0; z < hexagonalGrid.getLockedHexagons().size(); z++) {//залоченные фигуры
                        ArrayList<Integer> coordinate = hexagonalGrid.getLockedHexagons().get(z);
                        if (coordinate.size() != 0)
                            for (int x : coordinate) {
                                Optional<Hexagon> hexagonOptional = hexagonalGrid.getByAxialCoordinate(fromCoordinates(x, z));
                                drawPoly(canvas, convertToPointsArr(hexagonOptional.get().getPoints(), array), "#FF5346", Style.FILL);
                            }
                    }


                    Paint p = new Paint();
                    p.setColor(Color.parseColor("#81AA21"));
                    p.setStrokeWidth(1);
                    p.setStyle(Style.FILL_AND_STROKE);
                    p.setTextSize(40);
                    canvas.drawText("score: " + point, 30, (float) scrh - 15, p);
                    return false;

            }

        if (hexagonalGrid.getHexagonStorage().isEmpty()) {
            hexagonalGrid.getHexagonStorage().trimToSize();
            heapFigure.getFigure(gWidth);

            for (AxialCoordinate axialCoordinate : hexagonalGrid.getHexagonStorage())
            if (hexagonalGrid.getLockedHexagons().get(axialCoordinate.getGridZ()).contains(axialCoordinate.getGridX())) //условие выхода из игр
            return true;
        }

        canvas.drawColor(Color.parseColor("#001B2024")); // полностью прозрачный канвас

        //TODO: другим цветом рисовать т-ку поворота
        for (AxialCoordinate axialCoordinate : hexagonalGrid.getHexagonStorage()) { //фигруа
            //флажочек и если первый то строк остальные фил
            drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(axialCoordinate).get().getPoints(), array), "#81AA21", Style.FILL);
        }
        return false;
    }


    private void drawPoly(Canvas canvas, int[] array, String color,  Style style) {
        if (array.length < 12) return;

        Paint p = new Paint();
        p.setColor(Color.parseColor(color));
        p.setStyle(style);
        p.setStrokeWidth(2);

        Path polyPath = new Path();
        polyPath.moveTo(array[0], array[1]);

        for (int i = 0; i < 12;  i=i+2 )
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

    private double rad(int gWidth, int gHeight)
    {
        radius = 2*scrw/(Math.sqrt(3)*(2*gWidth+1));
        int parallax = 50;
        if ((radius*(gHeight / 2 + gHeight + (Math.sqrt(3) / 2 / 2))) > (scrh-parallax) && gHeight % 2 == 0)
            radius = (scrh-parallax) / (gHeight / 2 + gHeight + (Math.sqrt(3) / 2 / 2));
        else if ((radius*( gHeight + ((gHeight+1) /2))) > (scrh-parallax) && gHeight % 2 != 0)
            radius = (scrh-parallax) / ( gHeight + ((gHeight+1) /2));

        return radius;
    }
}