package api;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.LinkedList;

import backport.Optional;
import rx.Observable;


public interface HexagonalGrid {

    Observable<Hexagon> getHexagons();

    boolean containsAxialCoordinate(AxialCoordinate coordinate);

    Optional<Hexagon> getByAxialCoordinate(AxialCoordinate coordinate);

    SparseArray<ArrayList<Integer>> getLockedHexagons();

    ArrayList<AxialCoordinate> getHexagonStorage ();

    LinkedList<Hexagon> getNeighborsOf(Hexagon hexagon);

    int getWidth();

}