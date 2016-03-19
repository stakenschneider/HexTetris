package api;


import internal.impl.layoutstrategy.TrapezoidGridLayoutStrategy;
import internal.impl.layoutstrategy.GridLayoutStrategy;
import internal.impl.layoutstrategy.HexagonalGridLayoutStrategy;
import internal.impl.layoutstrategy.RectangularGridLayoutStrategy;
import internal.impl.layoutstrategy.TriangularGridLayoutStrategy;


public enum HexagonalGridLayout {

    /**
     * A rectangular layout has no special rules.
     */
    RECTANGULAR(new RectangularGridLayoutStrategy()),

    /**
     * The hexagonal layout must have equal width and height and
     * it must be odd.
     */
    HEXAGONAL(new HexagonalGridLayoutStrategy()),

    /**
     * A triangular layout must have equal width and height.
     */
    TRIANGULAR(new TriangularGridLayoutStrategy()),

    /**
     * A trapezoid layout has no special rules.
     */
    TRAPEZOID(new TrapezoidGridLayoutStrategy());

    private GridLayoutStrategy gridLayoutStrategy;

    HexagonalGridLayout(final GridLayoutStrategy gridLayoutStrategy) {
        this.gridLayoutStrategy = gridLayoutStrategy;
    }

    /**
     * Checks whether the grid height/width parameters can be used for the given {@link GridLayoutStrategy}.
     *
     * @return valid?
     */
    boolean checkParameters(final int gridHeight, final int gridWidth) {
        return getGridLayoutStrategy().checkParameters(gridHeight, gridWidth);
    }

    public GridLayoutStrategy getGridLayoutStrategy() {
        return gridLayoutStrategy;
    }
}