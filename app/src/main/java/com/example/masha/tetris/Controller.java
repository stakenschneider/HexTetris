package com.example.masha.tetris;

import java.util.ArrayList;

import internal.impl.HexagonData;
import static api.AxialCoordinate.fromCoordinates;


public class Controller {

    private  ArrayList<HexagonData> dataMap; //лист падающих фигур
    public int lastFigure;
    public Controller( ArrayList<HexagonData> dataMap)
    {
        this.dataMap = dataMap;
        lastFigure = 0;
    }


    public ArrayList<HexagonData> moveDownRight()
    {
        for (HexagonData data : dataMap)
            if (data.partOfLocked!=true)
                data.coordinate.setGridZ(data.coordinate.getGridZ() + 1);
        return dataMap;
    }

    public ArrayList<HexagonData> moveDownLeft()
    {
        for (HexagonData data : dataMap)
            if (data.partOfLocked!=true) {
                data.coordinate.setGridZ(data.coordinate.getGridZ() + 1);
                data.coordinate.setGridX(data.coordinate.getGridX()-1);
            }

            else
                {
                    dataMap.get(i).coordinate.setGridZ(dataMap.get(i).coordinate.getGridZ() + 1);
                    if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).coordinate.getGridX()-1, dataMap.get(i).coordinate.getGridZ()+1)).isPresent())
                       for (int j = dataMap.size()-lastFigure; j<dataMap.size(); j++) dataMap.get(j).partOfLocked=true;
                }
        }
        return dataMap;
    }


    public ArrayList<HexagonData> moveRight()
    {
        for (int i = dataMap.size()-lastFigure; i<dataMap.size(); i++)
        {
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).coordinate.getGridX()+1, dataMap.get(i).coordinate.getGridZ())).isPresent())
                dataMap.get(i).coordinate.setGridX(dataMap.get(i).coordinate.getGridX() + 1);
            else {
                for (int j = dataMap.size() - lastFigure; j < i; j++)
                    dataMap.get(j).coordinate.setGridX(dataMap.get(j).coordinate.getGridX() - 1);
                break;
            }

        }
        return dataMap;
    }


    public ArrayList<HexagonData> moveLeft()
    {
        for (int i = dataMap.size()-lastFigure; i<dataMap.size(); i++)
        {
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap.get(i).coordinate.getGridX()-1, dataMap.get(i).coordinate.getGridZ())).isPresent())
                dataMap.get(i).coordinate.setGridX(dataMap.get(i).coordinate.getGridX() - 1);
            else {
                for (int j = dataMap.size() - lastFigure; j < i; j++)
                    dataMap.get(j).coordinate.setGridX(dataMap.get(j).coordinate.getGridX() - 1);
                break;
            }
        }
        return dataMap;
    }

    public ArrayList<HexagonData> rotation()
    {

        return dataMap;
    }

}