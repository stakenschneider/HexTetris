package api;

import backport.Optional;

import java.util.List;


public interface Hexagon {

    String getId();

    List<Point> getPoints();

    AxialCoordinate getAxialCoordinate();

    int getGridX();

    int getGridY();

    int getGridZ();

    double getCenterX();

    double getCenterY();

    <T extends SatelliteData> Optional<T> getSatelliteData();

    <T extends SatelliteData> void setSatelliteData(T data);

    void clearSatelliteData();

}