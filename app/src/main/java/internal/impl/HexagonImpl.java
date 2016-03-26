package internal.impl;


import android.util.Log;

import api.AxialCoordinate;
import api.Hexagon;
import api.Point;
import internal.GridData;

import java.util.ArrayList;
import java.util.List;
import backport.Optional;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static api.HexagonOrientation.FLAT_TOP;
import static api.Point.fromPosition;

/**
 * Default implementation of the {@link Hexagon} interface.
 */

public class HexagonImpl implements Hexagon {

    private final AxialCoordinate coordinate;
    private final transient GridData sharedData;
    private final transient ArrayList<HexagonData> dataMap;
    private static final String TAG = "myLogs";

    private HexagonImpl(final GridData gridData, final AxialCoordinate coordinate, ArrayList<HexagonData> dataMap) {
        this.sharedData = gridData;
        this.coordinate = coordinate;
        this.dataMap = dataMap;
    }

    /**
     * Creates a new {@link Hexagon} object from shared data and a coordinate.
     */
    public static Hexagon newHexagon(final GridData gridData, final AxialCoordinate coordinate, ArrayList<HexagonData> dataMap) {
        return new HexagonImpl(gridData, coordinate, dataMap);
    }

    @Override
    public String getId() {
        return coordinate.toKey();
    }

    @Override
    public final List<Point> getPoints() {
        final List<Point> points = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            final double angle = 2 * Math.PI / 6 * (i + sharedData.getOrientation().getCoordinateOffset());
            final double x = getCenterX() + sharedData.getRadius() * cos(angle);
            final double y = getCenterY() + sharedData.getRadius() * sin(angle);
            points.add(fromPosition(x, y));
        }
        return points;
    }
    @Override
    public  HexagonData getHexagonData() {
        for (HexagonData data : dataMap)
        {
            if ((getAxialCoordinate().getGridX()==data.coordinate.getGridX())&(getAxialCoordinate().getGridZ()==data.coordinate.getGridZ()))
            {
                return data;
            }

        }
        HexagonData data = new HexagonData(false,false,getAxialCoordinate());
        return data;
    }
    @Override
    public void setState(boolean state1 , boolean state2)
    {
        HexagonData data = new HexagonData(state1,state2, getAxialCoordinate());
        dataMap.add(data);
    }

    @Override
    public AxialCoordinate getAxialCoordinate() {
        return coordinate;
    }

    @Override
    public int getGridX() {
        return coordinate.getGridX();
    }

    @Override
    public final int getGridY() {
        return -(coordinate.getGridX() + coordinate.getGridZ());
    }

    @Override
    public int getGridZ() {
        return coordinate.getGridZ();
    }

    @Override
    public final double getCenterX() {
        if (FLAT_TOP.equals(sharedData.getOrientation())) {
            return coordinate.getGridX() * sharedData.getHexagonWidth() + sharedData.getRadius();
        } else {
            return coordinate.getGridX() * sharedData.getHexagonWidth() + coordinate.getGridZ()
                    * sharedData.getHexagonWidth() / 2 + sharedData.getHexagonWidth() / 2;
        }
    }

    @Override
    public final double getCenterY() {
        if (FLAT_TOP.equals(sharedData.getOrientation())) {
            return coordinate.getGridZ() * sharedData.getHexagonHeight() + coordinate.getGridX()
                    * sharedData.getHexagonHeight() / 2 + sharedData.getHexagonHeight() / 2;
        } else {
            return coordinate.getGridZ() * sharedData.getHexagonHeight() + sharedData.getRadius();
        }
    }
}
