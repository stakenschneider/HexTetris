package internal.impl;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGridBuilder;
import api.Point;
import backport.Optional;
import internal.GridData;

import api.CoordinateConverter;
import api.HexagonalGrid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;


public final class HexagonalGridImpl implements HexagonalGrid {

    private static final int[][] NEIGHBORS = {{+1, 0}, {+1, -1}, {0, -1}, {-1, 0}, {-1, +1}, {0, +1}};
    private static final int NEIGHBOR_X_INDEX = 0;
    private static final int NEIGHBOR_Z_INDEX = 1;

    private final GridData gridData;
    private final Map<AxialCoordinate, Object> hexagonStorage;
    private final Set<AxialCoordinate> coordinates;


    public HexagonalGridImpl(final HexagonalGridBuilder builder) {
        this.gridData = builder.getGridData();
        this.hexagonStorage = builder.getCustomStorage();
        this.coordinates = builder.getGridLayoutStrategy().fetchGridCoordinates(builder);
    }

    @Override
    public Iterable<Hexagon> getHexagons() {
        ArrayList <Hexagon> Hexagons = new  ArrayList();
        Iterator<AxialCoordinate> iterator = coordinates.iterator();
        while(iterator.hasNext()) {
            Hexagons.add(HexagonImpl.newHexagon(gridData, iterator.next(), hexagonStorage));
        }
        iterator.remove();
        return Hexagons;
    }

    @Override
    public Iterable<Hexagon> getHexagonsByAxialRange(final AxialCoordinate from, final AxialCoordinate to) {
        ArrayList <Hexagon> Hexagons = new  ArrayList();
        for (int x = from.getGridX(); x <= to.getGridX(); x++)
        {
            for (int z = from.getGridZ(); z <= to.getGridZ(); z++)
            {
                if(getByAxialCoordinate(AxialCoordinate.fromCoordinates(x, z)).isPresent())
                {
                    Hexagons.add(getByAxialCoordinate(AxialCoordinate.fromCoordinates(x, z)).get());
                }
            }
        }
        return Hexagons;
    }

    @Override
    public Iterable<Hexagon> getHexagonsByOffsetRange(final int gridXFrom, final int gridXTo, final int gridYFrom, final int gridYTo) {
        ArrayList<Hexagon> Hexagons = new ArrayList();
        for (int x = gridXFrom; x <= gridXTo; x++) {
            for (int y = gridYFrom; y <= gridYTo; y++) {
                final int axialX = CoordinateConverter.convertOffsetCoordinatesToAxialX(x, y, gridData.getOrientation());
                final int axialZ = CoordinateConverter.convertOffsetCoordinatesToAxialZ(x, y, gridData.getOrientation());
                if(getByAxialCoordinate(AxialCoordinate.fromCoordinates(axialX, axialZ)).isPresent())
                {
                    Hexagons.add(getByAxialCoordinate(AxialCoordinate.fromCoordinates(axialX, axialZ)).get());
                }
            }
        }


        return Hexagons;
    }

    @Override
    public boolean containsAxialCoordinate(final AxialCoordinate coordinate) {
        return this.coordinates.contains(coordinate);
    }

    @Override
    public Optional<Hexagon> getByAxialCoordinate(final AxialCoordinate coordinate) {
        return containsAxialCoordinate(coordinate)
                ? Optional.of(HexagonImpl.newHexagon(gridData, coordinate, hexagonStorage))
                : Optional.empty();
    }

    @Override
    public Optional<Hexagon> getByPixelCoordinate(final double coordinateX, final double coordinateY) {
        int estimatedGridX = (int) (coordinateX / gridData.getHexagonWidth());
        int estimatedGridZ = (int) (coordinateY / gridData.getHexagonHeight());
        estimatedGridX = CoordinateConverter.convertOffsetCoordinatesToAxialX(estimatedGridX, estimatedGridZ, gridData.getOrientation());
        estimatedGridZ = CoordinateConverter.convertOffsetCoordinatesToAxialZ(estimatedGridX, estimatedGridZ, gridData.getOrientation());
        // it is possible that the estimated coordinates are off the grid so we
        // create a virtual hexagon
        final AxialCoordinate estimatedCoordinate = AxialCoordinate.fromCoordinates(estimatedGridX, estimatedGridZ);
        final Hexagon tempHex = HexagonImpl.newHexagon(gridData, estimatedCoordinate, hexagonStorage);

        Hexagon trueHex = refineHexagonByPixel(tempHex, Point.fromPosition(coordinateX, coordinateY));

        if (hexagonsAreAtTheSamePosition(tempHex, trueHex)) {
            return getByAxialCoordinate(estimatedCoordinate);
        } else {
            return containsAxialCoordinate(trueHex.getAxialCoordinate()) ? Optional.of(trueHex) : Optional.empty();
        }
    }

    @Override
    public Iterable<Hexagon> getNeighborsOf(final Hexagon hexagon) {
        final Set<Hexagon> neighbors = new HashSet<>();
        for (final int[] neighbor : NEIGHBORS) {
            Hexagon retHex;
            final int neighborGridX = hexagon.getGridX() + neighbor[NEIGHBOR_X_INDEX];
            final int neighborGridZ = hexagon.getGridZ() + neighbor[NEIGHBOR_Z_INDEX];
            final AxialCoordinate neighborCoordinate = AxialCoordinate.fromCoordinates(neighborGridX, neighborGridZ);
            if (containsAxialCoordinate(neighborCoordinate)) {
                retHex = getByAxialCoordinate(neighborCoordinate).get();
                neighbors.add(retHex);
            }
        }
        return neighbors;
    }

    @Override
    public void clearSatelliteData() {
        hexagonStorage.clear();
    }

    @Override
    public GridData getGridData() {
        return gridData;
    }

    private boolean hexagonsAreAtTheSamePosition(final Hexagon hex0, final Hexagon hex1) {
        return hex0.getGridX() == hex1.getGridX() && hex0.getGridZ() == hex1.getGridZ();
    }

    private Hexagon refineHexagonByPixel(final Hexagon hexagon, final Point clickedPoint) {
        Hexagon refined = hexagon;
        double smallestDistance = clickedPoint.distanceFrom(Point.fromPosition(refined.getCenterX(), refined.getCenterY()));
        for (final Hexagon neighbor : getNeighborsOf(hexagon)) {
            final double currentDistance = clickedPoint.distanceFrom(Point.fromPosition(neighbor.getCenterX(), neighbor.getCenterY()));
            if (currentDistance < smallestDistance) {
                refined = neighbor;
                smallestDistance = currentDistance;
            }
        }
        return refined;
    }
}
