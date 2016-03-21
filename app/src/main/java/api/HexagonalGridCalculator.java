package api;

import java.util.Set;


public interface HexagonalGridCalculator {

    int calculateDistanceBetween(Hexagon hex0, Hexagon hex1);

    Set<Hexagon> calculateMovementRangeFrom(Hexagon hexagon, int distance);
}
