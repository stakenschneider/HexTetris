package internal.impl;


import api.AxialCoordinate;

public class HexagonData {
    public int X;
    public boolean partOfFigure;
    public boolean partOfLocked;
    public AxialCoordinate coordinate;
    public HexagonData(boolean state1, boolean state2, AxialCoordinate coordinate1)
    {
        partOfFigure = state1;
        partOfLocked = state2;
        coordinate = coordinate1;
        X = coordinate.getGridX();
    }
    public void set(boolean state1, boolean state2, AxialCoordinate coordinate1)
    {
        partOfFigure = state1;
        partOfLocked = state2;
        coordinate = coordinate1;
    }
}
