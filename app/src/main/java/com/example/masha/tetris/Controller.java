package com.example.masha.tetris;

import java.util.ArrayList;

import internal.impl.HexagonData;


public class Controller {

    private  ArrayList<HexagonData> dataMap; //лист падающих фигур


    public Controller( ArrayList<HexagonData> dataMap)
    {
        this.dataMap = dataMap;
    }


    public ArrayList<HexagonData> movedown()
    {
        for (HexagonData data : dataMap)
            if (data.partOfLocked!=true) {
                data.coordinate.setGridZ(data.coordinate.getGridZ() + 1);
                if (dataMap.get(0).coordinate.getGridZ()%2==0)
                    data.coordinate.setGridX(data.coordinate.getGridX()-1);
            }

        return dataMap;
    }


    public ArrayList<HexagonData> moveright()
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


    public ArrayList<HexagonData> moveleft()
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

}