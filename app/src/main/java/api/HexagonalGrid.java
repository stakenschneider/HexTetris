package api;

import android.util.SparseArray;

import java.util.ArrayList;

import backport.Optional;
import rx.Observable;


public interface HexagonalGrid {

    Observable<Hexagon> getHexagons();

    boolean containsAxialCoordinate(AxialCoordinate coordinate);

    Optional<Hexagon> getByAxialCoordinate(AxialCoordinate coordinate);

    SparseArray<ArrayList<Integer>> getLockedHexagons();

    ArrayList<AxialCoordinate> getHexagonStorage ();

    int getWidth();

}