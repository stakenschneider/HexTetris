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
import java.util.Set;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import static internal.impl.HexagonImpl.newHexagon;
import static com.example.masha.tetris.Settings.height;


public class HexagonalGridImpl implements HexagonalGrid {

    private final GridData gridData;
    public ArrayList<AxialCoordinate>  hexagonStorage;
    private final Set<AxialCoordinate> coordinates;
    private SparseArray <ArrayList<Integer>> lockedHexagons = new SparseArray<ArrayList<Integer>> ();
    private int width ;


    public HexagonalGridImpl(final HexagonalGridBuilder builder) {
        this.gridData = builder.getGridData();
        this.hexagonStorage = builder.getCustomStorage();
        this.coordinates = builder.getGridLayoutStrategy().fetchGridCoordinates(builder);
        this.width = builder.getGridWidth();

        if (height<15) height = 15;
        for ( int i = 0; i<height; i++) {
            ArrayList coordinates = new ArrayList();
            lockedHexagons.put(i,coordinates);
        }
    }

    @Override
    public SparseArray <ArrayList<Integer>> getLockedHexagons()
    {
        return lockedHexagons;
    }

    public ArrayList<AxialCoordinate> getHexagonStorage ()
    {
        return hexagonStorage;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public Observable<Hexagon> getHexagons() {
        Observable<Hexagon> result = Observable.create(
                (Subscriber<? super Hexagon> subscriber) -> {
                    final Iterator<AxialCoordinate> coordinateIterator = coordinates.iterator();
                    while (coordinateIterator.hasNext()) {
                        subscriber.onNext(newHexagon(gridData, coordinateIterator.next(), hexagonStorage, lockedHexagons));
                    }
                    subscriber.onCompleted();
                }
        );
        return result;
    }

    @Override
    public boolean containsAxialCoordinate(final AxialCoordinate coordinate) {
        return this.coordinates.contains(coordinate);
    }

    @Override
    public  Optional<Hexagon> getByAxialCoordinate(final AxialCoordinate coordinate) {
        return containsAxialCoordinate(coordinate)
                ? Optional.of(newHexagon(gridData, coordinate, hexagonStorage, lockedHexagons))
                : Optional.empty();
    }
}