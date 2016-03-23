package com.example.masha.tetris;


import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import api.AxialCoordinate;
import static api.CoordinateConverter.convertToCol;
import static api.CoordinateConverter.convertToRow;
import static api.CoordinateConverter.convertOffsetCoordinatesToAxialX;
import api.Hexagon;
import static api.HexagonOrientation.POINTY_TOP;

public class Figure {
    private static final String TAG = "myLogs";


    private class OffsetCoordinate
    {
        public int col;
        public int row;
        public OffsetCoordinate(int x, int y)
        {
            col = x;
            row = y;
        }
    };
    int quantity;
    int maxcol = -1;
    int dx;
    int dy;
    ArrayList <Hexagon> Hexagons = new  ArrayList();

    public Figure()
    {

    }


    public AxialCoordinate convertToGrid (int width, ArrayList <Hexagon> hex)
    {
        if (hex==null) Log.d(TAG, "Jeppa");
        Hexagons = hex;
        int firstcol;
        int firstrow;
        OffsetCoordinate offsetCoordinate = placeFirst();
        int change = 0;
            if ((maxcol-offsetCoordinate.col) % 2 == 0)
            {
                change= (width/2)-((maxcol-offsetCoordinate.col+1)/2)-1;
            }
            else
            {
                change= (width/2)-((maxcol-offsetCoordinate.col+1)/2);
            }
        firstrow = Hexagons.get(0).getGridZ()- offsetCoordinate.row;
        firstcol = convertToCol(Hexagons.get(0).getGridX(), Hexagons.get(0).getGridZ(), POINTY_TOP)- (offsetCoordinate.col-change);
        firstcol = convertOffsetCoordinatesToAxialX(firstcol,firstrow,POINTY_TOP);
        AxialCoordinate coordinate = new AxialCoordinate(firstcol,firstrow);
        dy = offsetCoordinate.row;
        dx = offsetCoordinate.col-change;
        return coordinate;
    }

    public AxialCoordinate getNewCoordinate(Hexagon hex)
    {
        int row = hex.getGridZ()-dy;
        int col = 0;
        if (row%2!=0) {
            col = convertToCol(hex.getGridX(), hex.getGridZ(), POINTY_TOP) - dx - 1;
        }
        else
        {
            col = convertToCol(hex.getGridX(), hex.getGridZ(), POINTY_TOP) - dx;
        }

        col = convertOffsetCoordinatesToAxialX(col,row,POINTY_TOP);
        AxialCoordinate coordinate = new AxialCoordinate(col,row);
        return coordinate;
    }

    private OffsetCoordinate placeFirst ()
    {
        AxialCoordinate coordinate;
        int mincol = -1;
        int minrow = -1;

        Iterator<Hexagon> iterator = Hexagons.iterator();
        do {
            coordinate = iterator.next().getAxialCoordinate();
            int currentcol = convertToCol(coordinate.getGridX(),coordinate.getGridZ(),POINTY_TOP);
            int currentrow = convertToRow(coordinate.getGridZ());
            if (currentcol<mincol||mincol==-1)
            {
                mincol = currentcol;
            }
            if (currentcol>maxcol||mincol==-1)
            {
                maxcol = currentcol;
            }
            if (currentrow<minrow||minrow==-1)
            {
                minrow = currentrow;
            }
        } while(iterator.hasNext());
        OffsetCoordinate offsetCoordinate = new OffsetCoordinate(mincol,minrow);
        return offsetCoordinate;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
