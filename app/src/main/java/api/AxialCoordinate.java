package api;


import static java.lang.Integer.parseInt;


public final class AxialCoordinate  {

    private int gridX;
    private int gridZ;

    public AxialCoordinate( int gridX, int gridZ) {
        this.gridX = gridX;
        this.gridZ = gridZ;
    }

    public static AxialCoordinate fromKey(String key) {
        AxialCoordinate result;
        try {
            final String[] coords = key.split(",");
            result = fromCoordinates(parseInt(coords[0]), parseInt(coords[1]));
        } catch (final Exception e) {
            throw new IllegalArgumentException("Failed to create AxialCoordinate from key: " + key, e);
        }
        return result;
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

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + gridX;
        result = prime * result + gridZ;
        return result;
    }

    public int getGridX()
    {
        return gridX;
    }

    public int getGridZ() {return gridZ;}


    public static AxialCoordinate fromCoordinates( int gridX, int gridZ) {
        return new AxialCoordinate(gridX, gridZ);
    }

    public void setGridX ( int x)
    {
        gridX = x;
    }

    public void setGridZ ( int z)
    {
        gridZ = z;
    }

    public void setCoordinate( int x,  int z)
    {
        gridX = x;
        gridZ = z;
    }

    public String toKey() {
        return gridX + "," + gridZ;
    }

}