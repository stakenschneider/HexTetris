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

    protected int quantity , maxCol = -1, dx , dy;
    ArrayList <Hexagon> Hexagons;

    public Figure(ArrayList<Hexagon> hex) {
        Hexagons = hex;
        quantity = Hexagons.size();
    }


    public AxialCoordinate convertToGrid (int width) {
        int firstCol , firstRow;

        OffsetCoordinate offsetCoordinate = placeFirst();

        if ((maxCol-offsetCoordinate.col) % 2 == 0)
            dx =offsetCoordinate.col-((width/2)-((maxCol-offsetCoordinate.col+1)/2)-1);
        else
            dx =offsetCoordinate.col-((width/2)-((maxCol-offsetCoordinate.col+1)/2));

        firstRow = Hexagons.get(0).getGridZ()- offsetCoordinate.row;
        firstCol = convertToCol(Hexagons.get(0).getGridX(), Hexagons.get(0).getGridZ())- dx;
        firstCol = convertOffsetCoordinatesToAxialX(firstCol,firstRow);

        AxialCoordinate coordinate = new AxialCoordinate(firstCol,firstRow);
        dy = offsetCoordinate.row;

        return coordinate;
    }


    public AxialCoordinate getNewCoordinate(Hexagon hex) {
        int row = hex.getGridZ() - dy , col;

        if (row%2 != 0 & dy != 0) col = convertToCol(hex.getGridX(), hex.getGridZ()) - dx - 1;
        else col = convertToCol(hex.getGridX(), hex.getGridZ()) - dx;

        col = convertOffsetCoordinatesToAxialX(col,row);

        return new AxialCoordinate(col,row);
    }


    private OffsetCoordinate placeFirst (){
        AxialCoordinate coordinate;
        int minCol = -1 , minRow = -1;

        Iterator<Hexagon> iterator = Hexagons.iterator();

        do {
            coordinate = iterator.next().getAxialCoordinate();
            int currentCol = convertToCol(coordinate.getGridX(),coordinate.getGridZ()),
                    currentRow = convertToRow(coordinate.getGridZ());

            if (currentCol < minCol || minCol == -1)
                minCol = currentCol;

            if (currentCol > maxCol || minCol == -1)
                maxCol = currentCol;

            if (currentRow < minRow || minRow == -1)
                minRow = currentRow;

        } while(iterator.hasNext());

        return new OffsetCoordinate( minCol , minRow );
    }
}
