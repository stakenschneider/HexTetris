package com.example.masha.tetris;

import android.util.SparseArray;
import java.util.ArrayList;

import api.AxialCoordinate;
import api.HexagonalGrid;
import backport.Optional;

import static api.AxialCoordinate.fromCoordinates;


public class   Controller {

    private ArrayList<AxialCoordinate> dataMap;  //фигуры в движении
    public SparseArray<ArrayList<Integer>> lockedHexagons; // заблокированные шестиугольники
    private int point; //очки

    public Controller( ArrayList<AxialCoordinate> dataMap, SparseArray <ArrayList<Integer>> lockedHexagons, int point) {
        this.dataMap = dataMap;
        this.lockedHexagons = lockedHexagons;
        this.point = point;
    }


    private boolean check (int i, HexagonalGrid hexagonalGrid , int n ) {
        final int GridZ = dataMap.get(i).gridZ;
        final int GridX = dataMap.get(i).gridX;
                if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(GridX -n, GridZ+1)).isPresent() || lockedHexagons.get(GridZ+1).contains(GridX  - n))
            return false;
        return true;
    }


    public int moveDownRight(HexagonalGrid hexagonalGrid) {
        for (int i = 0; i<dataMap.size(); i++) {
            if (!check(i, hexagonalGrid, 0)){
                for (int j = 0; j < i; j++) {
                    dataMap.get(j).setGridZ(dataMap.get(j).gridZ - 1);
                    lockedHexagons.get(dataMap.get(j).gridZ).add(dataMap.get(j).gridX);
                }

                for ( int j = i; j < dataMap.size(); j++)
                    lockedHexagons.get(dataMap.get(j).gridZ).add(dataMap.get(j).gridX);

                checkRow(hexagonalGrid);
                dataMap.clear();
                break;
            }
            dataMap.get(i).setGridZ(dataMap.get(i).gridZ + 1);
        }
        return point;
    }


    public int moveDownLeft(HexagonalGrid hexagonalGrid) {
        for (int i = 0; i<dataMap.size(); i++){
            if (!check(i, hexagonalGrid, 1))
            {
                for (int j = 0; j < i; j++) {
                    dataMap.get(j).setGridZ(dataMap.get(j).gridZ - 1);
                    dataMap.get(j).setGridX(dataMap.get(j).gridX + 1);
                    lockedHexagons.get(dataMap.get(j).gridZ).add(dataMap.get(j).gridX);     //а вот и он! С просонья наверн не увидел)
                }

                for (int j = i ; j < dataMap.size(); j++)
                    lockedHexagons.get(dataMap.get(j).gridZ).add(dataMap.get(j).gridX);

                checkRow(hexagonalGrid);
                dataMap.clear();
                break;
            }

            dataMap.get(i).setGridZ(dataMap.get(i).gridZ + 1);
            dataMap.get(i).setGridX(dataMap.get(i).gridX - 1);
        }
        return point;
    }


    public void moveRight(HexagonalGrid hexagonalGrid) {
        for (int i = 0; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).gridZ;
            final int GridX = dataMap.get(i).gridX;
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(GridX + 1, GridZ)).isPresent() & (!lockedHexagons.get(GridZ).contains(GridX + 1)))
                dataMap.get(i).setGridX(dataMap.get(i).gridX + 1);
            else {
                for (int j = 0; j < i; j++)
                    dataMap.get(j).setGridX(dataMap.get(j).gridX - 1);
                break;
            }
        }
    }


    public void moveLeft(HexagonalGrid hexagonalGrid) {
        for (int i = 0; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).gridZ;
            final int GridX = dataMap.get(i).gridX;
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(GridX-1, GridZ)).isPresent()&(!lockedHexagons.get(GridZ).contains(GridX-1)))
                dataMap.get(i).setGridX(GridX  - 1);
            else {
                for (int j = 0; j < i; j++)
                    dataMap.get(j).setGridX(GridX  + 1);
                break;
            }
        }
    }


    public void rotationClockwise(HexagonalGrid hexagonalGrid) {
        int x = dataMap.get(0).gridX , z = dataMap.get(0).gridZ , y = - x - z;
        boolean b = true;


        for (int i = 1; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).gridZ;
            final int GridX = dataMap.get(i).gridX;
            if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(GridZ - z) + x, -(-GridX - GridZ - y) + z)).isPresent() ||
                    (Optional.ofNullable(lockedHexagons.get((-(-GridX - GridZ - y) + z))).isPresent()&lockedHexagons.get((-(-GridX - GridZ - y) + z)).contains(-(GridZ - z) + x))) {
                b = false;
                break;}
        }
        if (b)
            for (int i = 1; i<dataMap.size(); i++)
                dataMap.get(i).setCoordinate(-(dataMap.get(i).gridZ - z) + x, -(-dataMap.get(i).gridX - dataMap.get(i).gridZ - y) + z);

    }

    public void rotationCounterClockwise(HexagonalGrid hexagonalGrid) {
        int x = dataMap.get(0).gridX ,  z = dataMap.get(0).gridZ , y = - x - z;
        boolean b = true;

        for (int i = 1; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).gridZ;
            final int GridX = dataMap.get(i).gridX;
            if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(-GridX - GridZ - y) + x, -(GridX - x) + z)).isPresent()
                    || (Optional.ofNullable(lockedHexagons.get(-(GridX - x) + z)).isPresent() & lockedHexagons.get(-(GridX - x) + z).contains(-(-GridX - GridZ - y) + x))) {
                b = false;
                break;
            }
        }

        if (b)
            for (int i = 1; i<dataMap.size(); i++)
                dataMap.get(i).setCoordinate(-(-dataMap.get(i).gridX - dataMap.get(i).gridZ - y) + x , -(dataMap.get(i).gridX - x) + z);
    }


    private void checkRow(HexagonalGrid hexagonalGrid) {
        for (AxialCoordinate data : dataMap)
            if (lockedHexagons.get(data.gridZ).size() == hexagonalGrid.getWidth()) {
                point= hexagonalGrid.getWidth()+point;
                lockedHexagons.get(data.gridZ).clear();
                lockedHexagons.get(data.gridZ).trimToSize();

                for (int i = data.gridZ; i > 0; i--)
                    if ((i-1)%2==0) {
                        ArrayList<Integer> coordinate = new ArrayList<Integer>(lockedHexagons.get(i - 1).size());
                        for (Integer x : lockedHexagons.get(i-1)) coordinate.add(x);
                        lockedHexagons.put(i, coordinate);
                    }
                    else {
                        ArrayList<Integer>  coordinate = new ArrayList<Integer>(lockedHexagons.get(i - 1).size());
                        for (Integer x : lockedHexagons.get(i-1)) coordinate.add(x-1);
                        lockedHexagons.put(i, coordinate);
                    }
            }
    }
}