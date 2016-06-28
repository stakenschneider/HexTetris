package wrrrrrm;

import android.util.SparseArray;
import java.util.ArrayList;

import api.AxialCoordinate;
import api.HexagonalGrid;
import backport.Optional;
import static com.example.masha.tetris.GamePlay.h;

import static api.AxialCoordinate.fromCoordinates;

//TODO: get -> flatMap?!

public class Controller {

    private ArrayList<AxialCoordinate> dataMap;
    public SparseArray<ArrayList<Integer>> lockedHexagons;
    private int point;

    public Controller( ArrayList<AxialCoordinate> dataMap, SparseArray <ArrayList<Integer>> lockedHexagons, int point) {
        this.dataMap = dataMap;
        this.lockedHexagons = lockedHexagons;
        this.point = point;
    }


    private boolean check (int i, HexagonalGrid hexagonalGrid , int n ) {
        final int GridZ = dataMap.get(i).getGridZ();
        final int GridX = dataMap.get(i).getGridX();
        if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(GridX -n, GridZ+1)).isPresent() || lockedHexagons.get(GridZ+1).contains(GridX  - n))
            return false;
        return true;
    }


    public int moveDownRight(HexagonalGrid hexagonalGrid) {
        dataMap.get(0).setGridZ(dataMap.get(0).getGridZ() + 1);
        for (int i = 1; i<dataMap.size(); i++) {
            if (!check(i, hexagonalGrid, 0)){
                dataMap.get(0).setGridZ(dataMap.get(0).getGridZ() - 1);
                for (int j = 1; j < i; j++) {
                    dataMap.get(j).setGridZ(dataMap.get(j).getGridZ() - 1);
                    lockedHexagons.get(dataMap.get(j).getGridZ()).add(dataMap.get(j).getGridX());
                }
                for ( int j = i; j < dataMap.size(); j++)
                    lockedHexagons.get(dataMap.get(j).getGridZ()).add(dataMap.get(j).getGridX());
                checkRow(hexagonalGrid);
                dataMap.clear();
                break;
            }
            dataMap.get(i).setGridZ(dataMap.get(i).getGridZ() + 1);
        }
        return point;
    }


    public int moveDownLeft(HexagonalGrid hexagonalGrid) {
        dataMap.get(0).setGridZ(dataMap.get(0).getGridZ() + 1);
        dataMap.get(0).setGridX(dataMap.get(0).getGridX() - 1);
        for (int i = 1; i<dataMap.size(); i++){
            if (!check(i, hexagonalGrid, 1))
            {
                dataMap.get(0).setGridZ(dataMap.get(0).getGridZ() - 1);
                dataMap.get(0).setGridX(dataMap.get(0).getGridX() + 1);
                for (int j = 1; j < i; j++) {
                    dataMap.get(j).setGridZ(dataMap.get(j).getGridZ() - 1);
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() + 1);
                    lockedHexagons.get(dataMap.get(j).getGridZ()).add(dataMap.get(j).getGridX());
                }
                for (int j = i ; j < dataMap.size(); j++)
                    lockedHexagons.get(dataMap.get(j).getGridZ()).add(dataMap.get(j).getGridX());
                checkRow(hexagonalGrid);
                dataMap.clear();
                break;
            }
            dataMap.get(i).setGridZ(dataMap.get(i).getGridZ() + 1);
            dataMap.get(i).setGridX(dataMap.get(i).getGridX() - 1);
        }
        return point;
    }


    public void moveRight(HexagonalGrid hexagonalGrid) {
        dataMap.get(0).setGridX(dataMap.get(0).getGridX()+ 1);
        for (int i = 1; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).getGridZ();
            final int GridX = dataMap.get(i).getGridX();
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(GridX + 1, GridZ)).isPresent() & (!lockedHexagons.get(GridZ).contains(GridX + 1)))
                dataMap.get(i).setGridX(dataMap.get(i).getGridX() + 1);
            else {
                for (int j = 0; j < i; j++)
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() - 1);
                break;
            }
        }
    }


    public void moveLeft(HexagonalGrid hexagonalGrid) {
        dataMap.get(0).setGridX(dataMap.get(0).getGridX()- 1);
        for (int i = 1; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).getGridZ();
            final int GridX = dataMap.get(i).getGridX();
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(GridX-1, GridZ)).isPresent()&(!lockedHexagons.get(GridZ).contains(GridX-1))) {
                dataMap.get(i).setGridX(GridX - 1);
            }
            else {
                for (int j = 0; j < i; j++)
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX()  + 1);
                break;
            }
        }
    }


    public void rotationClockwise(HexagonalGrid hexagonalGrid) {
        int x = dataMap.get(0).getGridX() , z = dataMap.get(0).getGridZ() , y = - x - z;
        boolean b = true;

        for (int i = 1; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).getGridZ();
            final int GridX = dataMap.get(i).getGridX();
            if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(GridZ - z) + x, -(-GridX - GridZ - y) + z)).isPresent() ||
                    (Optional.ofNullable(lockedHexagons.get((-(-GridX - GridZ - y) + z))).isPresent()&lockedHexagons.get((-(-GridX - GridZ - y) + z)).contains(-(GridZ - z) + x))) {
                b = false;
                break;}
        }
        if (b)
            for (int i = 1; i<dataMap.size(); i++)
                dataMap.get(i).setCoordinate(-(dataMap.get(i).getGridZ() - z) + x, -(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + z);

    }


    public void rotationCounterClockwise(HexagonalGrid hexagonalGrid) {
        int x = dataMap.get(0).getGridX(), z = dataMap.get(0).getGridZ() ,y = - x - z;
        boolean b = true;

        for (int i = 1; i<dataMap.size(); i++) {
            final int GridZ = dataMap.get(i).getGridZ();
            final int GridX = dataMap.get(i).getGridX();
            if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(-GridX - GridZ - y) + x, -(GridX - x) + z)).isPresent()
                    || (Optional.ofNullable(lockedHexagons.get(-(GridX - x) + z)).isPresent() & lockedHexagons.get(-(GridX - x) + z).contains(-(-GridX - GridZ - y) + x))) {
                b = false;
                break;
            }
        }
        if (b)
            for (int i = 1; i<dataMap.size(); i++)
                dataMap.get(i).setCoordinate(-(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + x , -(dataMap.get(i).getGridX() - x) + z);
    }


    private void checkRow(HexagonalGrid hexagonalGrid) {
        for (int j = 1; j<dataMap.size(); j++)
            if (lockedHexagons.get(dataMap.get(j).getGridZ()).size() == hexagonalGrid.getWidth()) {
                point= hexagonalGrid.getWidth()+point;
                lockedHexagons.get(dataMap.get(j).getGridZ()).clear();
                lockedHexagons.get(dataMap.get(j).getGridZ()).trimToSize();

                for (int i = dataMap.get(j).getGridZ(); i > 0; i--)
                    if ((i-1)%2==0) {
                        ArrayList<Integer> coordinate = new ArrayList<>(lockedHexagons.get(i - 1).size());
                        for (Integer x : lockedHexagons.get(i-1)) coordinate.add(x);
                        lockedHexagons.put(i, coordinate);
                    }
                    else {
                        ArrayList<Integer>  coordinate = new ArrayList<>(lockedHexagons.get(i - 1).size());
                        for (Integer x : lockedHexagons.get(i-1)) coordinate.add(x-1);
                        lockedHexagons.put(i, coordinate);
                    }
            }
        Thread t = new Thread(() -> h.sendEmptyMessage(1));
        t.start();
    }
}