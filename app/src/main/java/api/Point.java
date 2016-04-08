package api;

public final class Point {

    private final double coordinateX;
    private final double coordinateY;

    private Point(final double coordinateX, final double coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public static Point fromPosition(final double coordinateX, final double coordinateY) {return new Point(coordinateX, coordinateY);}
    
    public double getCoordinateX()
    {
        return coordinateX;
    }
    
    public double getCoordinateY()
    {
        return coordinateY;
    }

}
