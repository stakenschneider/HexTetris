package wrrrrrm;

import java.util.ArrayList;
import java.util.Iterator;

import api.AxialCoordinate;
import api.Hexagon;

import static api.CoordinateConverter.convertOffsetCoordinatesToAxialX;
import static api.CoordinateConverter.convertToCol;
import static api.CoordinateConverter.convertToRow;


public class Figure {

    private class OffsetCoordinate {
        public int col , row;
        public OffsetCoordinate(int x, int y) {
            col = x;
            row = y;
        }
    }

    int quantity , maxCol = -1, dx , dy;
    ArrayList <Hexagon> Hexagons;

    public Figure(ArrayList<Hexagon> hex) {
        Hexagons = hex;
        quantity = Hexagons.size();
    }


    public AxialCoordinate convertToGrid (int width)
    {
        int firstcol , firstrow;

        OffsetCoordinate offsetCoordinate = placeFirst();

        if ((maxCol-offsetCoordinate.col) % 2 == 0)
            dx =offsetCoordinate.col-((width/2)-((maxCol-offsetCoordinate.col+1)/2)-1);
        else
            dx =offsetCoordinate.col-((width/2)-((maxCol-offsetCoordinate.col+1)/2));

        firstrow = Hexagons.get(0).getGridZ()- offsetCoordinate.row;
        firstcol = convertToCol(Hexagons.get(0).getGridX(), Hexagons.get(0).getGridZ())- dx;
        firstcol = convertOffsetCoordinatesToAxialX(firstcol,firstrow);

        AxialCoordinate coordinate = new AxialCoordinate(firstcol,firstrow);
        dy = offsetCoordinate.row;

        return coordinate;
    }


    public AxialCoordinate getNewCoordinate(Hexagon hex)
    {
        int row = hex.getGridZ()-dy , col;

        if (row%2!=0&dy!=0) col = convertToCol(hex.getGridX(), hex.getGridZ()) - dx - 1;
        else col = convertToCol(hex.getGridX(), hex.getGridZ()) - dx;

        col = convertOffsetCoordinatesToAxialX(col,row);
        AxialCoordinate coordinate = new AxialCoordinate(col,row);

        return coordinate;
    }


    private OffsetCoordinate placeFirst ()
    {
        AxialCoordinate coordinate;
        int mincol = -1 , minrow = -1;

        Iterator<Hexagon> iterator = Hexagons.iterator();

        do {
            coordinate = iterator.next().getAxialCoordinate();
            int currentcol = convertToCol(coordinate.getGridX(),coordinate.getGridZ()),
                    currentrow = convertToRow(coordinate.getGridZ());

            if (currentcol<mincol||mincol==-1)
                mincol = currentcol;

            if (currentcol>maxCol||mincol==-1)
                maxCol = currentcol;

            if (currentrow<minrow||minrow==-1)
                minrow = currentrow;

        } while(iterator.hasNext());

        OffsetCoordinate offsetCoordinate = new OffsetCoordinate( mincol , minrow );

        return offsetCoordinate;
    }
}
