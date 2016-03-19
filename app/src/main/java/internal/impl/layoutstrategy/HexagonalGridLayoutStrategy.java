package internal.impl.layoutstrategy;

import api.AxialCoordinate;
import api.HexagonOrientation;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;

import java.util.LinkedHashSet;
import java.util.Set;
import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.round;


public final class HexagonalGridLayoutStrategy implements GridLayoutStrategy {

    @Override
    public Set<AxialCoordinate> fetchGridCoordinates(HexagonalGridBuilder builder) {
        Set<AxialCoordinate> coordinates = new LinkedHashSet<>();
        final double gridSize = builder.getGridHeight();
        int startX = HexagonOrientation.FLAT_TOP.equals(builder.getOrientation()) ? (int) floor(gridSize / 2d) : (int) round(gridSize / 4d);
        final int hexRadius = (int) floor(gridSize / 2d);
        final int minX = startX - hexRadius;
        for (int y = 0; y < gridSize; y++) {
            final int distanceFromMid = abs(hexRadius - y);
            for (int x = max(startX, minX); x <= max(startX, minX) + hexRadius + hexRadius - distanceFromMid; x++) {
                final int gridX = x;
                final int gridZ = HexagonOrientation.FLAT_TOP.equals(builder.getOrientation()) ? y - (int) floor(gridSize / 4d) : y;
                coordinates.add(AxialCoordinate.fromCoordinates(gridX, gridZ));
            }
            startX--;
        }
        return coordinates;
    }

    @Override
    public boolean checkParameters(final int gridHeight, final int gridWidth) {
        final boolean superResult = GridLayoutStrategy.super.checkParameters(gridHeight, gridWidth);
        final boolean result = gridHeight == gridWidth && abs(gridHeight % 2) == 1;
        return result && superResult;
    }

}
