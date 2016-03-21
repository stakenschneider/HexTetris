package api;

import java.util.List;
import backport.Optional;


public interface Hexagon {

    String getId();

    List<Point> getPoints();

    AxialCoordinate getAxialCoordinate();

    int getGridX();

    int getGridY();

    int getGridZ();

    double getCenterX();

    double getCenterY();
}
