package api;

import internal.impl.layoutstrategy.GridLayoutStrategy;
import internal.impl.layoutstrategy.RectangularGridLayoutStrategy;


public enum HexagonalGridLayout {

    RECTANGULAR(new RectangularGridLayoutStrategy());

    private GridLayoutStrategy gridLayoutStrategy;

    HexagonalGridLayout(final GridLayoutStrategy gridLayoutStrategy) {
        this.gridLayoutStrategy = gridLayoutStrategy;
    }

    public GridLayoutStrategy getGridLayoutStrategy() {
        return gridLayoutStrategy;
    }
}
