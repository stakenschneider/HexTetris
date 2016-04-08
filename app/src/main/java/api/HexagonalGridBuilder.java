package api;

import internal.GridData;
import internal.impl.HexagonalGridCalculatorImpl;
import internal.impl.HexagonalGridImpl;
import internal.impl.layoutstrategy.GridLayoutStrategy;

import java.util.ArrayList;

import static api.HexagonalGridLayout.RECTANGULAR;


public final class HexagonalGridBuilder {
    private int gridWidth ,  gridHeight;
    private double radius;
    private ArrayList<AxialCoordinate>  customStorage = new ArrayList<>();
    private HexagonOrientation orientation = HexagonOrientation.POINTY_TOP;
    private HexagonalGridLayout gridLayout = RECTANGULAR;


    public HexagonalGrid build() {
        return new HexagonalGridImpl(this);
    }

    public HexagonalGridCalculator buildCalculatorFor(final HexagonalGrid hexagonalGrid) {
        return new HexagonalGridCalculatorImpl(hexagonalGrid);
    }

    public HexagonalGridBuilder setRadius(final double radius) {
        this.radius = radius;
        return this;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public HexagonalGridBuilder setGridWidth(final int gridWidth) {
        this.gridWidth = gridWidth;
        return this;
    }

    public HexagonalGridBuilder setGridHeight(final int gridHeight) {
        this.gridHeight = gridHeight;
        return this;
    }

    public HexagonalGridBuilder setOrientation(final HexagonOrientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public GridLayoutStrategy getGridLayoutStrategy() {
        return gridLayout.getGridLayoutStrategy();
    }

    public ArrayList<AxialCoordinate>  getCustomStorage() {
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
