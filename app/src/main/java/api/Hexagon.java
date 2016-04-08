package api;

import java.util.List;


public interface Hexagon {

    String getId();

    List<Point> getPoints();

    AxialCoordinate getAxialCoordinate();

    void setState();

    int getGridX();

    int getGridY();

    int getGridZ();

    double getCenterX();

    double getCenterY();
}
