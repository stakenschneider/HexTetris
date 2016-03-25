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
    ArrayList <Hexagon> Hexagons;

    public Figure(ArrayList <Hexagon> hex)
    {
        Hexagons = hex;
        quantity = Hexagons.size();
    }


    public AxialCoordinate convertToGrid (int width)
    {
        int firstcol;
        int firstrow;
        OffsetCoordinate offsetCoordinate = placeFirst();
<<<<<<< HEAD
        if ((maxcol-offsetCoordinate.col) % 2 == 0)
        {
            dx =offsetCoordinate.col-((width/2)-((maxcol-offsetCoordinate.col+1)/2)-1);
        }
        else
        {
            dx =offsetCoordinate.col-((width/2)-((maxcol-offsetCoordinate.col+1)/2));
        }
=======
            if ((maxcol-offsetCoordinate.col) % 2 == 0)
            {
                dx =offsetCoordinate.col-((width/2)-((maxcol-offsetCoordinate.col+1)/2)-1);
            }
            else
            {
                dx =offsetCoordinate.col-((width/2)-((maxcol-offsetCoordinate.col+1)/2));
            }
>>>>>>> origin/master
        firstrow = Hexagons.get(0).getGridZ()- offsetCoordinate.row;
        firstcol = convertToCol(Hexagons.get(0).getGridX(), Hexagons.get(0).getGridZ())- dx;
        firstcol = convertOffsetCoordinatesToAxialX(firstcol,firstrow);
        AxialCoordinate coordinate = new AxialCoordinate(firstcol,firstrow);
        dy = offsetCoordinate.row;
        return coordinate;
    }

    public AxialCoordinate getNewCoordinate(Hexagon hex)
    {
        int row = hex.getGridZ()-dy;
        int col = 0;
        if (row%2!=0) {
            col = convertToCol(hex.getGridX(), hex.getGridZ()) - dx - 1;
        }
        else
        {
            col = convertToCol(hex.getGridX(), hex.getGridZ()) - dx;
        }
        col = convertOffsetCoordinatesToAxialX(col,row);
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
            int currentcol = convertToCol(coordinate.getGridX(),coordinate.getGridZ());
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
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/master
