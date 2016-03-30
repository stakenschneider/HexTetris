package api;

import internal.impl.layoutstrategy.GridLayoutStrategy;
import internal.impl.layoutstrategy.RectangularGridLayoutStrategy;


public enum HexagonalGridLayout {

    RECTANGULAR(new RectangularGridLayoutStrategy());

    private GridLayoutStrategy gridLayoutStrategy;

    HexagonalGridLayout(final GridLayoutStrategy gridLayoutStrategy) {
        this.gridLayoutStrategy = gridLayoutStrategy;
    }

    boolean checkParameters(final int gridHeight, final int gridWidth) {
        return getGridLayoutStrategy().checkParameters(gridHeight, gridWidth);
    }

    public GridLayoutStrategy getGridLayoutStrategy() {
        return gridLayoutStrategy;
    }
}
