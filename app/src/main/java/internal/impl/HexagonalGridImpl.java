package internal.impl;

import android.util.SparseArray;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;
import api.HexagonalGridBuilder;
import internal.GridData;

import java.util.ArrayList;
import java.util.Iterator;
import backport.Optional;

import java.util.LinkedList;

import java.util.Set;
import rx.Observable;
import rx.Subscriber;

import static internal.impl.HexagonImpl.newHexagon;
import static api.AxialCoordinate.fromCoordinates;


public class HexagonalGridImpl implements HexagonalGrid {

    private final GridData gridData;
    public ArrayList<AxialCoordinate>  hexagonStorage;
    private final Set<AxialCoordinate> coordinates;
    private SparseArray <ArrayList<Integer>> lockedHexagons = new SparseArray<ArrayList<Integer>> ();
    private int width ;
    private int height;


    private static final int[][] NEIGHBORS = {{+1, 0}, {-1, 0}, {-1, +1}, {0, +1}};
    private static final int NEIGHBOR_X_INDEX = 0;
    private static final int NEIGHBOR_Z_INDEX = 1;


    public HexagonalGridImpl(final HexagonalGridBuilder builder) {
        this.gridData = builder.getGridData();
        this.hexagonStorage = builder.getCustomStorage();
        this.coordinates = builder.getGridLayoutStrategy().fetchGridCoordinates(builder);
        this.width = builder.getGridWidth();
        this.height = builder.getGridHeight();

        if (height<15) height = 15;
        for ( int i = 0; i<height; i++) {
            ArrayList coordinates = new ArrayList();
            lockedHexagons.put(i,coordinates);
        }
    }

    @Override
    public SparseArray <ArrayList<Integer>> getLockedHexagons() {return lockedHexagons;}

    public ArrayList<AxialCoordinate> getHexagonStorage () {return hexagonStorage;}

    @Override
    public int getWidth() {return width;}

    @Override
    public int getHeight() {return height;}

    @Override
    public Observable<Hexagon> getHexagons() {
        Observable<Hexagon> result = Observable.create((Subscriber<? super Hexagon> subscriber) -> {
            final Iterator<AxialCoordinate> coordinateIterator = coordinates.iterator();
            while (coordinateIterator.hasNext()) {
                subscriber.onNext(newHexagon(gridData, coordinateIterator.next(), hexagonStorage, lockedHexagons));
            }
            subscriber.onCompleted();
        });
        return result;
    }

    @Override
    public boolean containsAxialCoordinate(final AxialCoordinate coordinate) {
        return this.coordinates.contains(coordinate);
    }

    @Override
    public LinkedList<Hexagon> getNeighborsOf(final Hexagon hexagon) {
        final LinkedList<Hexagon> neighbors = new LinkedList<>();
        for (final int[] neighbor : NEIGHBORS) {
            Hexagon retHex;
            final int neighborGridX = hexagon.getGridX() + neighbor[NEIGHBOR_X_INDEX];
            final int neighborGridZ = hexagon.getGridZ() + neighbor[NEIGHBOR_Z_INDEX];
            final AxialCoordinate neighborCoordinate = fromCoordinates(neighborGridX, neighborGridZ);
            if (containsAxialCoordinate(neighborCoordinate)) {
                retHex = getByAxialCoordinate(neighborCoordinate).get();
                neighbors.addFirst(retHex);
            }
        }
        return neighbors;
    }

    @Override
    public  Optional<Hexagon> getByAxialCoordinate(final AxialCoordinate coordinate) {
        return containsAxialCoordinate(coordinate)
                ? Optional.of(newHexagon(gridData, coordinate, hexagonStorage, lockedHexagons))
                : Optional.empty();
    }
}