package api;

import java.util.List;

import internal.impl.HexagonData;


public interface Hexagon {

    String getId();

    List<Point> getPoints();

    AxialCoordinate getAxialCoordinate();

    void setState(boolean state1 , boolean state2);

    HexagonData getHexagonData();

    int getGridX();

    int getGridY();

    int getGridZ();

    double getCenterX();

    double getCenterY();
}
