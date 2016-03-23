package internal.impl.layoutstrategy;

import api.AxialCoordinate;
import api.HexagonalGridBuilder;

import java.util.Set;


public interface GridLayoutStrategy {

    Set<AxialCoordinate> fetchGridCoordinates(HexagonalGridBuilder builder);

    default boolean checkParameters(final int gridHeight, final int gridWidth) {
        return gridHeight > 0 && gridWidth > 0;
    }

}
