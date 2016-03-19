package internal.impl.layoutstrategy;

import api.AxialCoordinate;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import api.CoordinateConverter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This strategy is responsible for generating a {@link HexagonalGrid} which has a rectangular
 * shape.
 */
public final class RectangularGridLayoutStrategy implements GridLayoutStrategy {

    @Override
    public Set<AxialCoordinate> fetchGridCoordinates(HexagonalGridBuilder builder) {
        Set<AxialCoordinate> coordinates = new LinkedHashSet<>();
        for (int y = 0; y < builder.getGridHeight(); y++) {
            for (int x = 0; x < builder.getGridWidth(); x++) {
                final int gridX = CoordinateConverter.convertOffsetCoordinatesToAxialX(x, y, builder.getOrientation());
                final int gridZ = CoordinateConverter.convertOffsetCoordinatesToAxialZ(x, y, builder.getOrientation());
                coordinates.add(AxialCoordinate.fromCoordinates(gridX, gridZ));
            }
        }
        return coordinates;
    }

    @Override
    public boolean checkParameters(final int gridHeight, final int gridWidth) {
        return GridLayoutStrategy.super.checkParameters(gridHeight, gridWidth);
    }
}