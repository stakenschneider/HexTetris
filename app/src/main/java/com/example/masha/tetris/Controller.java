package com.example.masha.tetris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import api.AxialCoordinate;
import api.HexagonalGrid;

import static com.example.masha.tetris.Settings.width;
import static com.example.masha.tetris.Settings.height;

import static api.AxialCoordinate.fromCoordinates;


public class Controller {

    private  ArrayList<AxialCoordinate> dataMap;               //хексы активной фигуры
    private  HashMap <AxialCoordinate,Integer> lockedHexagons; // залоченные хексы

    public Controller( ArrayList<AxialCoordinate> dataMap, HashMap <AxialCoordinate,Integer> lockedHexagons)
    {
        this.dataMap = dataMap;
        this.lockedHexagons = lockedHexagons;
    }

    private boolean check (int i, HexagonalGrid hexagonalGrid , int n )
    {
        if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).getGridX()-n,dataMap.get(i).getGridZ()+1)).isPresent() || (lockedHexagons.containsKey(fromCoordinates(dataMap.get(i).getGridX()-n, dataMap.get(i).getGridZ()+1))))
            return false;
        return true;
    }


    public void moveDownRight(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++) {
            if (!check(i, hexagonalGrid, 0)) {
                for (int j = 0; j < i; j++) {
                    dataMap.get(j).setGridZ(dataMap.get(j).getGridZ() - 1);
                    lockedHexagons.put(dataMap.get(j), dataMap.get(j).getGridZ()  );
                }

                for ( int j = i; j < dataMap.size(); j++)
                    lockedHexagons.put(dataMap.get(j),dataMap.get(j).getGridZ());

                dataMap.clear();
                break;
            }
            dataMap.get(i).setGridZ(dataMap.get(i).getGridZ() + 1);
        }
    }


    public void moveDownLeft(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++){
            if (!check(i, hexagonalGrid, 1)) {
                for (int j = 0; j < i; j++) {
                    dataMap.get(j).setGridZ(dataMap.get(j).getGridZ() - 1);
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() + 1);
                    lockedHexagons.put(dataMap.get(j), dataMap.get(j).getGridZ());
                }

                for (int j = i ; j < dataMap.size(); j++)
                    lockedHexagons.put(dataMap.get(j), dataMap.get(j).getGridZ());

                dataMap.clear();
                break;
            }
            dataMap.get(i).setGridZ(dataMap.get(i).getGridZ() + 1);
            dataMap.get(i).setGridX(dataMap.get(i).getGridX() - 1);
        }

    }


    public void moveRight(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++)
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).getGridX()+1,dataMap.get(i).getGridZ())).isPresent()&(!lockedHexagons.containsKey(fromCoordinates(dataMap.get(i).getGridX() + 1, dataMap.get(i).getGridZ()))))
                dataMap.get(i).setGridX(dataMap.get(i).getGridX() + 1);
            else {
                for (int j = 0; j < i; j++)
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() - 1);
                break;
            }
    }


    public void moveLeft(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++)
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).getGridX()-1,dataMap.get(i).getGridZ())).isPresent() & (!lockedHexagons.containsKey(fromCoordinates(dataMap.get(i).getGridX()-1,dataMap.get(i).getGridZ()))))
                    dataMap.get(i).setGridX(dataMap.get(i).getGridX() - 1);
            else {
                for (int j = 0; j < i; j++)
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() + 1);
                break;
            }

    }


    public void rotationClockwise(HexagonalGrid hexagonalGrid) {
        int x = dataMap.get(0).getGridX() , z = dataMap.get(0).getGridZ() , y = - x - z;
        boolean b = true;

        for (int i = 1; i<dataMap.size(); i++)
            //осторожно порнуха
            if (!(hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(dataMap.get(i).getGridZ() - z) + x,-(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + z)).isPresent() &!lockedHexagons.containsKey(fromCoordinates(-(dataMap.get(i).getGridZ() - z) + x,-(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + z))))
                b = false;

        if (b==true)
            for (int i = 1; i<dataMap.size(); i++)
                dataMap.get(i).setCoordinate(-(dataMap.get(i).getGridZ() - z) + x, -(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + z);

    }


    public void rotationCounterClockwise(HexagonalGrid hexagonalGrid) {
        int x = dataMap.get(0).getGridX() ,  z = dataMap.get(0).getGridZ() , y = - x - z;
        boolean b = true;

        for (int i = 1; i<dataMap.size(); i++)
            if (!(hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + x,-(dataMap.get(i).getGridX() - x) + z)).isPresent()&!lockedHexagons.containsKey(fromCoordinates(-(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + x,-(dataMap.get(i).getGridX() - x) + z))))
                b = false;

        if (b==true)
        for (int i = 1; i<dataMap.size(); i++)
            dataMap.get(i).setCoordinate(-(-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) + x , -(dataMap.get(i).getGridX() - x) + z);
    }


    public void deleteRow() {
        int count = 0;

        if (height < 15) height = 15;
        if (width < 8) width = 8;

        for (int i = 0; i < height; i++) //пробегаемся по все строчкам
        {
            for (int value : lockedHexagons.values()) //а теперь и по всем строчкам залоченым гексам
                if (value == i) //ищем совпадения
                    count++;

            if (count == width) //если количество гексов в строчке равно мощности строчки
                for (int j = 0; j < width; j++) //то все гексы
                    lockedHexagons.values().remove(i); //удаляем из этой строчки
            count = 0;
        }

    }




}