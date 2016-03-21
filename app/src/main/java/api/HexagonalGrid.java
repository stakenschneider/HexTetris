package api;

import internal.GridData;

import backport.Optional;


public interface HexagonalGrid {

    GridData getGridData();

    Iterable<Hexagon> getHexagons();

    Iterable<Hexagon> getHexagonsByAxialRange(AxialCoordinate from, AxialCoordinate to);

    Iterable<Hexagon> getHexagonsByOffsetRange(int gridXFrom, int gridXTo, int gridYfrom, int gridYTo);

    boolean containsAxialCoordinate(AxialCoordinate coordinate);

    Optional<Hexagon> getByAxialCoordinate(AxialCoordinate coordinate);

    Optional<Hexagon> getByPixelCoordinate(double coordinateX, double coordinateY);

    Iterable<Hexagon> getNeighborsOf(Hexagon hexagon);

    void clearSatelliteData();
}
