package api;

import static java.lang.Math.sqrt;


public final class Point {

    private final double coordinateX;
    private final double coordinateY;

    private Point(final double coordinateX, final double coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public static Point fromPosition(final double coordinateX, final double coordinateY) {
        return new Point(coordinateX, coordinateY);
    }

    public double distanceFrom(final Point point) {
        return sqrt((this.coordinateX - point.coordinateX) * (this.coordinateX - point.coordinateX)
                + (this.coordinateY - point.coordinateY) * (this.coordinateY - point.coordinateY));
    }
    
    public double getCoordinateX()
    {
        return coordinateX;
    }
    
    public double getCoordinateY()
    {
        return coordinateY;
    }
    
    
}
