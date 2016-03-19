package api;


import internal.impl.layoutstrategy.TrapezoidGridLayoutStrategy;
import internal.impl.layoutstrategy.GridLayoutStrategy;
import internal.impl.layoutstrategy.HexagonalGridLayoutStrategy;
import internal.impl.layoutstrategy.RectangularGridLayoutStrategy;
import internal.impl.layoutstrategy.TriangularGridLayoutStrategy;


public enum HexagonalGridLayout {

    RECTANGULAR(new RectangularGridLayoutStrategy()),

    HEXAGONAL(new HexagonalGridLayoutStrategy()),

    TRIANGULAR(new TriangularGridLayoutStrategy()),

    TRAPEZOID(new TrapezoidGridLayoutStrategy());

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