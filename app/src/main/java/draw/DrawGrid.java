package draw;

import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Color;

import AI.Mephistopheles;
import JSON.InitGame;
import wrrrrrm.Controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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

import static api.HexagonOrientation.POINTY_TOP;
import static api.HexagonalGridLayout.RECTANGULAR;
import static com.example.masha.tetris.GamePlay.h;
import static com.example.masha.tetris.Main.scrw;
import static com.example.masha.tetris.Main.scrh;
import static api.AxialCoordinate.fromCoordinates;
import static com.example.masha.tetris.Settings.height;
import static com.example.masha.tetris.Settings.width;

//TODO: добавить условие выигрыша + след игра по сидам + filled на открытии
public class DrawGrid {

    public HexagonalGridCalculator hexagonalGridCalculator;
    public HexagonalGrid hexagonalGrid;
    private Controller controller;
    protected HexagonOrientation orientation = POINTY_TOP;
    protected HexagonalGridLayout hexagonGridLayout = RECTANGULAR;
    public static int point;
    protected int gWidth = 0, gHeight = 0;
    private HeapFigure heapFigure;
    Mephistopheles ai;
    private String game;
    private LinkedList<String> path = new LinkedList<>();
    public int sourceL, colf = 0;


    public DrawGrid(String strJSON, String game) {
        this.game = game;
        InitGame initGame = new InitGame(strJSON);

        if (game.equals("UserParameters")) {
            gHeight = height;
            gWidth = width;
            if (height < 15) gHeight = initGame.height;
            if (width < 8) gWidth = initGame.width;
        } else if (game.equals("AiParameters")) {
            gHeight = initGame.height;
            gWidth = initGame.width;
        }

        Double radius = rad(gWidth, gHeight);
        point = 0;

        try {
            HexagonalGridBuilder builder = new HexagonalGridBuilder()
                    .setGridWidth(gWidth)
                    .setGridHeight(gHeight)
                    .setRadius(radius)
                    .setOrientation(orientation)
                    .setGridLayout(hexagonGridLayout);
            hexagonalGrid = builder.build();

            for (int i = 0; i < initGame.filled.size(); i += 2) {
                ArrayList<Integer> a = new ArrayList<>();
                a.add(initGame.filled.get(i));
                for (int j = 2 + i; j < initGame.filled.size(); j += 2) {
                    if (initGame.filled.get(i + 1) == initGame.filled.get(j + 1)) {
                        a.add(initGame.filled.get(j));
                        initGame.filled.remove(j + 1);
                        initGame.filled.remove(j);
                        j = j - 2;
                    }
                }
                hexagonalGrid.getLockedHexagons().put(initGame.filled.get(i + 1), a);
            }

            hexagonalGridCalculator = builder.buildCalculatorFor(hexagonalGrid);
            controller = new Controller(builder.getCustomStorage(), hexagonalGrid.getLockedHexagons(), point);
        } catch (HexagonalGridCreationException e) {
        }


        heapFigure = new HeapFigure(hexagonalGrid, initGame.quantityHexOfUnit.length, strJSON);
        heapFigure.makePRS(10, 0, BigInteger.valueOf(17));

        sourceL = initGame.sourceLength;
    }


    public boolean useBuilder(Canvas canvas, String movement) {

        if (game.equals("AiParameters") && path != null && !movement.equals("GAME") && !movement.equals("LOCKED") && !movement.equals("START"))
            movement = path.poll();

        int[] array = new int[12];
        switch (movement) {
            case "COUNTER_CLCK":
                controller.rotationCounterClockwise(hexagonalGrid);
                break;

            case "CLCK":
                controller.rotationClockwise(hexagonalGrid);
                break;

            case "DOWN_RIGHT":
                if (game.equals("UserParameters"))
                    point = controller.moveDownRight(hexagonalGrid);
                else controller.moveDownRight(hexagonalGrid);
                break;

            case "DOWN_LEFT":
                if (game.equals("UserParameters"))
                    point = controller.moveDownLeft(hexagonalGrid);
                else controller.moveDownLeft(hexagonalGrid);
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
                canvas.drawColor(Color.parseColor("#1B2024"));
                hexagonalGrid.getHexagons().forEach((Hexagon hexagon) ->
                        drawPoly(canvas, convertToPointsArr(hexagon.getPoints(), array), "#FF5346", Style.STROKE));
                return false;

            case "LOCKED":
                for (int z = 0; z < hexagonalGrid.getLockedHexagons().size(); z++) {//залоченные фигуры
                    ArrayList<Integer> coordinate = hexagonalGrid.getLockedHexagons().get(z);
                    if (coordinate.size() != 0)
                        for (int x : coordinate) {
                            Optional<Hexagon> hexagonOptional = hexagonalGrid.getByAxialCoordinate(fromCoordinates(x, z));
                            drawPoly(canvas, convertToPointsArr(hexagonOptional.get().getPoints(), array), "#FFD700", Style.FILL, Style.STROKE);
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


        //удаление ряда
        if (path != null && path.size() == 0 && game.equals("AiParameters")) {
            if (hexagonalGrid.getHexagonStorage().size() != 0)
                hexagonalGrid.getHexagonStorage().remove(0);

            for (AxialCoordinate axialCoordinate : hexagonalGrid.getHexagonStorage())
                hexagonalGrid.getLockedHexagons().get(axialCoordinate.getGridZ()).add(axialCoordinate.getGridX());
            for (int j = 0; j < hexagonalGrid.getHexagonStorage().size(); j++)
                if ((hexagonalGrid.getLockedHexagons().get(hexagonalGrid.getHexagonStorage().get(j).getGridZ()).size() == hexagonalGrid.getWidth())) {
                    point = hexagonalGrid.getWidth() + point;
                    hexagonalGrid.getLockedHexagons().get(hexagonalGrid.getHexagonStorage().get(j).getGridZ()).clear();
                    hexagonalGrid.getLockedHexagons().get(hexagonalGrid.getHexagonStorage().get(j).getGridZ()).trimToSize();

                    for (int i = hexagonalGrid.getHexagonStorage().get(j).getGridZ(); i > 0; i--)
                        if ((i - 1) % 2 == 0) {
                            ArrayList<Integer> coordinate = new ArrayList<>(hexagonalGrid.getLockedHexagons().get(i - 1).size());
                            for (Integer x : hexagonalGrid.getLockedHexagons().get(i - 1))
                                coordinate.add(x);
                            hexagonalGrid.getLockedHexagons().put(i, coordinate);
                        } else {
                            ArrayList<Integer> coordinate = new ArrayList<>(hexagonalGrid.getLockedHexagons().get(i - 1).size());
                            for (Integer x : hexagonalGrid.getLockedHexagons().get(i - 1))
                                coordinate.add(x - 1);
                            hexagonalGrid.getLockedHexagons().put(i, coordinate);
                        }
                }
            hexagonalGrid.getHexagonStorage().clear();
            Thread t = new Thread(() -> h.sendEmptyMessage(1));
            t.start();
        }


        if (hexagonalGrid.getHexagonStorage().isEmpty()) {
            hexagonalGrid.getHexagonStorage().trimToSize();

            colf++;
            //TODO: в этом месте второй параметр должен менятся в случае если игра закончилась И есть еще переметры сиды
            if (sourceL == colf) {
                return true;
            } else heapFigure.getFigure(gWidth, 0);

            if (game.equals("AiParameters")) {
                ai = new Mephistopheles(hexagonalGrid, hexagonalGridCalculator);
                path = ai.startSearch(hexagonalGrid.getHexagonStorage());

                if (path == null)
                    return true;
                else Collections.reverse(path);
            }

            for (AxialCoordinate axialCoordinate : hexagonalGrid.getHexagonStorage())
                if (hexagonalGrid.getLockedHexagons().get(axialCoordinate.getGridZ()).contains(axialCoordinate.getGridX())) //условие выхода из игр
                    return true;
        }


        int first = 0;
        for (AxialCoordinate axialCoordinate : hexagonalGrid.getHexagonStorage()) //фигруа
            if (hexagonalGrid.containsAxialCoordinate(axialCoordinate)) {
                if (first == 0) {
                    drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(axialCoordinate).get().getPoints(), array), "#F0F0F0", Style.STROKE);
                    first = 1;
                } else
                    drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(axialCoordinate).get().getPoints(), array), "#81AA21", Style.FILL, Style.STROKE);
            } else first = 1;

        return false;
    }


    private void drawPoly(Canvas canvas, int[] array, String color, Style style) {
        if (array.length < 12) return;

        Paint p = new Paint();
        p.setColor(Color.parseColor(color));
        p.setStyle(style);
        p.setStrokeWidth(2);

        Path polyPath = new Path();
        polyPath.moveTo(array[0], array[1]);

        for (int i = 0; i < 12; i = i + 2)
            polyPath.lineTo(array[i], array[i + 1]);

        polyPath.lineTo(array[0], array[1]);
        canvas.drawPath(polyPath, p);
    }

    private void drawPoly(Canvas canvas, int[] array, String color, Style style, Style style1) {
        if (array.length < 12) return;

        Paint p = new Paint();
        p.setColor(Color.parseColor(color));
        p.setStyle(style);
        p.setStrokeWidth(2);

        Paint p1 = new Paint();
        p1.setColor(Color.parseColor("#FF5346"));
        p1.setStyle(style1);
        p1.setStrokeWidth(2);

        Path polyPath = new Path();
        polyPath.moveTo(array[0], array[1]);

        for (int i = 0; i < 12; i = i + 2)
            polyPath.lineTo(array[i], array[i + 1]);

        polyPath.lineTo(array[0], array[1]);
        canvas.drawPath(polyPath, p);
        canvas.drawPath(polyPath, p1);
    }


    private int[] convertToPointsArr(List<Point> points, int[] array) {
        int idx = 0;
        for (Point point : points) {
            array[idx] = (int) Math.round(point.getCoordinateX());
            array[idx + 1] = (int) Math.round(point.getCoordinateY());
            idx = idx + 2;
        }
        return array;
    }


    private double rad(int gWidth, int gHeight) {
        Double radius = 2 * scrw / (Math.sqrt(3) * (2 * gWidth + 1));
        int parallax = 50;
        if ((radius * (gHeight / 2 + gHeight + (Math.sqrt(3) / 2 / 2))) > (scrh - parallax) && gHeight % 2 == 0)
            radius = (scrh - parallax) / (gHeight / 2 + gHeight + (Math.sqrt(3) / 2 / 2));
        else if ((radius * (gHeight + ((gHeight + 1) / 2))) > (scrh - parallax) && gHeight % 2 != 0)
            radius = (scrh - parallax) / (gHeight + ((gHeight + 1) / 2));

        return radius;
    }
}