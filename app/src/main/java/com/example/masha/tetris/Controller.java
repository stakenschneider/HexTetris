package com.example.masha.tetris;

import java.util.ArrayList;

import internal.impl.HexagonData;


public class Controller {

    private  ArrayList<HexagonData> dataMap; //лист падающих фигур


    public Controller( ArrayList<HexagonData> dataMap)
    {
        this.dataMap = dataMap;
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

        return dataMap;
    }


    public ArrayList<HexagonData> moveRight()
    {
        for (HexagonData data : dataMap)
        {
            if (data.partOfLocked!=true) {

                data.coordinate.setGridX(data.coordinate.getGridX() + 1);
                data.X=data.X+1;
            }

        }
        return dataMap;
    }


    public ArrayList<HexagonData> moveLeft()
    {
        for (HexagonData data : dataMap)
        {
            if (data.partOfLocked!=true) {

                data.coordinate.setGridX(data.coordinate.getGridX() - 1);
                data.X = data.X-1;
            }
        }
        return dataMap;
    }

    public ArrayList<HexagonData> rotation()
    {

        return dataMap;
    }

}