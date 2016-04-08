package api;

import android.util.SparseArray;

import java.util.ArrayList;

import backport.Optional;


public interface HexagonalGrid {

    Iterable<Hexagon> getHexagons();

    boolean containsAxialCoordinate(AxialCoordinate coordinate);

    Optional<Hexagon> getByAxialCoordinate(AxialCoordinate coordinate);

    SparseArray<ArrayList> getLockedHexagons();

    ArrayList<AxialCoordinate> getHexagonStorage ();

    int getWidth();

}