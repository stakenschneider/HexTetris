package internal;

import api.HexagonOrientation;

import api.Hexagon;
import api.HexagonalGrid;
import api.HexagonalGridLayout;
import static java.lang.Math.sqrt;


public final class GridData {

    private final HexagonOrientation orientation;
    private final HexagonalGridLayout gridLayout;
    private final double radius;
    private final double hexagonHeight;
    private final double hexagonWidth;
    private final int gridWidth;
    private final int gridHeight;


    public GridData(final HexagonOrientation orientation, final HexagonalGridLayout gridLayout,
                    final double radius, int gridWidth, int gridHeight) {
        this.orientation = orientation;
        this.gridLayout = gridLayout;
        this.radius = radius;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.hexagonHeight = HexagonOrientation.FLAT_TOP.equals(orientation) ? calculateHeight(radius) : calculateWidth(radius);
        this.hexagonWidth = HexagonOrientation.FLAT_TOP.equals(orientation) ? calculateWidth(radius) : calculateHeight(radius);
    }

    private double calculateHeight(final double radius) {
        return sqrt(3) * radius;
    }

    public HexagonOrientation getOrientation()
    {
        return orientation;
    }

    public double getHexagonWidth()
    {
        return hexagonWidth;
    }

    public double getHexagonHeight()
    {
        return hexagonHeight;
    }

    public double getRadius()
    {
        return radius;
    }

    private double calculateWidth(final double radius) {
        return radius * 3 / 2;
    }
}