package internal.impl;

import android.util.SparseArray;

import api.AxialCoordinate;
import api.Hexagon;
import api.Point;
import internal.GridData;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static api.HexagonOrientation.FLAT_TOP;
import static api.Point.fromPosition;
import static api.AxialCoordinate.fromCoordinates;

public class HexagonImpl implements Hexagon {

    private final AxialCoordinate coordinate;
    private final transient GridData sharedData;
    private final transient ArrayList<AxialCoordinate> dataMap;
    private final transient SparseArray<ArrayList> lockedHexagons;

    private HexagonImpl(final GridData gridData, final AxialCoordinate coordinate, ArrayList<AxialCoordinate> dataMap , SparseArray<ArrayList> lockedHexagons) {
        this.sharedData = gridData;
        this.coordinate = coordinate;
        this.dataMap = dataMap;
        this.lockedHexagons = lockedHexagons;
    }

    public static Hexagon newHexagon(final GridData gridData, final AxialCoordinate coordinate, ArrayList<AxialCoordinate> dataMap , SparseArray<ArrayList> lockedHexagons) {
        return new HexagonImpl(gridData, coordinate, dataMap , lockedHexagons);
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
    public void setState() {
        AxialCoordinate data = fromCoordinates(getAxialCoordinate().getGridX(), getAxialCoordinate().getGridZ());
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
