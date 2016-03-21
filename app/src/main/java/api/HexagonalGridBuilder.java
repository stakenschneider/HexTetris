package api;

import api.exception.HexagonalGridCreationException;
import internal.GridData;
import internal.impl.HexagonalGridCalculatorImpl;
import internal.impl.HexagonalGridImpl;
import internal.impl.layoutstrategy.GridLayoutStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static api.HexagonalGridLayout.RECTANGULAR;


public final class HexagonalGridBuilder {
    private int gridWidth;
    private int gridHeight;
    private double radius;
    private Map<AxialCoordinate, Object> customStorage = new ConcurrentHashMap<>();
    private HexagonOrientation orientation = HexagonOrientation.POINTY_TOP;
    private HexagonalGridLayout gridLayout = RECTANGULAR;


    public HexagonalGrid build() {
        checkParameters();
        return new HexagonalGridImpl(this);
    }

    private void checkParameters() {
        if (orientation == null) {
            throw new HexagonalGridCreationException("Orientation must be set.");
        }
        if (radius <= 0) {
            throw new HexagonalGridCreationException("Radius must be greater than 0.");
        }
        if (gridLayout == null) {
            throw new HexagonalGridCreationException("Grid layout must be set.");
        }
        if (!gridLayout.checkParameters(gridHeight, gridWidth)) {
            throw new HexagonalGridCreationException("Width: " + gridWidth + " and height: " + gridHeight + " is not valid for: " + gridLayout.name() + " layout.");
        }
    }


    public HexagonalGridCalculator buildCalculatorFor(final HexagonalGrid hexagonalGrid) {
        return new HexagonalGridCalculatorImpl(hexagonalGrid);
    }

    public double getRadius() {
        return radius;
    }

    public HexagonalGridBuilder setRadius(final double radius) {
        this.radius = radius;
        return this;
    }

    public int getGridWidth() {
        return gridWidth;
    }


    public HexagonalGridBuilder setGridWidth(final int gridWidth) {
        this.gridWidth = gridWidth;
        return this;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public HexagonalGridBuilder setGridHeight(final int gridHeight) {
        this.gridHeight = gridHeight;
        return this;
    }

    public HexagonOrientation getOrientation() {
        return orientation;
    }


    public HexagonalGridBuilder setOrientation(final HexagonOrientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public GridLayoutStrategy getGridLayoutStrategy() {
        return gridLayout.getGridLayoutStrategy();
    }

    public Map<AxialCoordinate, Object> getCustomStorage() {
        return customStorage;
    }

    public GridData getGridData() {
        if (orientation == null || gridLayout == null || radius == 0 || gridWidth == 0 || gridHeight == 0) {
            throw new IllegalStateException("Not all necessary fields are initialized!");
        }
        return new GridData(orientation, gridLayout, radius, gridWidth, gridHeight);
    }


    public HexagonalGridBuilder setGridLayout(final HexagonalGridLayout gridLayout) {
        this.gridLayout = gridLayout;
        return this;
    }
}
