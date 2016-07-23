package api;

public final class AxialCoordinate implements Cloneable {

    private int gridX , gridZ;

    public AxialCoordinate( int gridX, int gridZ) {
        this.gridX = gridX;
        this.gridZ = gridZ;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AxialCoordinate other = (AxialCoordinate) obj;
        if (gridX != other.gridX)
            return false;
        if (gridZ != other.gridZ)
            return false;
        return true;
    }

    public AxialCoordinate clone() throws CloneNotSupportedException {
        return (AxialCoordinate)super.clone();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + gridX;
        result = prime * result + gridZ;
        return result;
    }

    public int getGridX() {return gridX;}

    public int getGridZ() {return gridZ;}

    public int getGridY() {return -getGridX()-getGridZ();}

    public static AxialCoordinate fromCoordinates( int gridX, int gridZ) {return new AxialCoordinate(gridX, gridZ);}

    public String toKey() {return gridX + "," + gridZ;}

    public void setGridX ( int x)
    {
        gridX = x;
    }

    public void setGridZ ( int z) {gridZ = z;}

    public void setCoordinate( int x,  int z) {
        gridX = x;
        gridZ = z;
    }
}