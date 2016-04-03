package com.example.masha.tetris;

import java.util.ArrayList;
import java.util.HashMap;

import api.AxialCoordinate;
import api.HexagonalGrid;

import static api.AxialCoordinate.fromCoordinates;


public class Controller {

    private  ArrayList<AxialCoordinate> dataMap; //хексы активной фигуры
    private  HashMap <AxialCoordinate,Integer> lockedHexagons; // залоченные хексы


    public Controller( ArrayList<AxialCoordinate> dataMap, HashMap <AxialCoordinate,Integer> lockedHexagons)
    {
        this.dataMap = dataMap;
        this.lockedHexagons = lockedHexagons;
    }


    public void moveDownRight(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++) {
            if (!checkDownRight(i, hexagonalGrid))  // проверка на столкновение
            {
                for (int j = 0; j < i; j++) {
                    dataMap.get(j).setGridZ(dataMap.get(j).getGridZ() - 1);   // если было столкновение, то предыдущие хексы делают шаг назад
                    lockedHexagons.put(dataMap.get(j),j);           // и сразу вносим их в список залоченных хексов
                }

                for ( int j = i; j < dataMap.size(); j++)         // заносим в список оставишиеся хексы, которым шаг назад не требовался
                    lockedHexagons.put(dataMap.get(j),j);

                dataMap.clear();                             // очищаем список активной фигуры, чтобы потом вызвать следующую
                break;
            }
            dataMap.get(i).setGridZ(dataMap.get(i).getGridZ() + 1);
        }
    }


    private boolean checkDownRight (int i, HexagonalGrid hexagonalGrid )
    {
        if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).getGridX(),
                dataMap.get(i).getGridZ() + 1)).isPresent() || (lockedHexagons.containsKey(fromCoordinates(dataMap.get(i).getGridX(),
                dataMap.get(i).getGridZ()+1))))
            return false;
        return true;
    }


    public void moveDownLeft(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++){
            if (!checkDownLeft(i, hexagonalGrid))
            {
                for (int j = 0; j < i; j++)
                {
                    dataMap.get(j).setGridZ(dataMap.get(j).getGridZ() - 1);
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() + 1);
                    lockedHexagons.put(dataMap.get(j),j);     //а вот и он! С просонья наверн не увидел)
                }
                for (int j = i ; j < dataMap.size(); j++)
                    lockedHexagons.put(dataMap.get(j),j);

                dataMap.clear();
                break;
            }

            dataMap.get(i).setGridZ(dataMap.get(i).getGridZ() + 1);
            dataMap.get(i).setGridX(dataMap.get(i).getGridX() - 1);
        }

    }


    private boolean checkDownLeft (int i, HexagonalGrid hexagonalGrid)
    {
        if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).getGridX()-1,
                dataMap.get(i).getGridZ() + 1)).isPresent() || (lockedHexagons.containsKey(fromCoordinates(dataMap.get(i).getGridX()-1,
                dataMap.get(i).getGridZ()+1))))
            return false;

        return true;
    }


    public void moveRight(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++)
        {
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).getGridX()+1, dataMap.get(i).getGridZ())).isPresent()&(!lockedHexagons.containsKey(fromCoordinates(dataMap.get(i).getGridX()+1,dataMap.get(i).getGridZ()))))
                dataMap.get(i).setGridX(dataMap.get(i).getGridX() + 1);
            else {
                for (int j = 0; j < i; j++)
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() - 1);
                break;
            }

        }
    }


    public void moveLeft(HexagonalGrid hexagonalGrid)
    {
        for (int i = 0; i<dataMap.size(); i++)
        {
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).getGridX()-1, dataMap.get(i).getGridZ())).isPresent()&(!lockedHexagons.containsKey(fromCoordinates(dataMap.get(i).getGridX()-1,dataMap.get(i).getGridZ()))))
                dataMap.get(i).setGridX(dataMap.get(i).getGridX() - 1);
            else {
                for (int j = dataMap.size(); j < i; j++)
                    dataMap.get(j).setGridX(dataMap.get(j).getGridX() - 1);
                break;
            }
        }
    }


    public void rotationClockwise() {
        int x = dataMap.get(0).getGridX();
        int y = -dataMap.get(0).getGridX()-dataMap.get(0).getGridZ();
        int z = dataMap.get(0).getGridZ();

        for (int i = 1; i<dataMap.size(); i++) {
            dataMap.get(i).setCoordinate((dataMap.get(i).getGridZ() - z) * (-1) + x, (-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) * (-1) + z);
        }
    }


    public void rotationCounterClockwise()
    {
        int x = dataMap.get(0).getGridX();
        int y = -dataMap.get(0).getGridX()-dataMap.get(0).getGridZ();
        int z = dataMap.get(0).getGridZ();

        for (int i = 1; i<dataMap.size(); i++) {
            dataMap.get(i).setCoordinate((-dataMap.get(i).getGridX() - dataMap.get(i).getGridZ() - y) * (-1) + x,(dataMap.get(i).getGridX() -x)*(-1)+z);
        }
    }

}