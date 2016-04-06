package api;


import static java.lang.Integer.parseInt;


public final class AxialCoordinate  {

    private int gridX ,  gridZ;

    public AxialCoordinate(final int gridX, final int gridZ) {
        this.gridX = gridX;
        this.gridZ = gridZ;
    }

    public static AxialCoordinate fromKey(final String key) {
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
        final int prime = 31;
        int result = 1;
        result = prime * result + gridX;
        result = prime * result + gridZ;
        return result;
    }

    public int getGridX() {return gridX;}

    public int getGridZ() {return gridZ;}

    public static AxialCoordinate fromCoordinates(final int gridX, final int gridZ) {return new AxialCoordinate(gridX, gridZ);}

    public void setGridX (final int x) {gridX = x;}

    public void setGridZ (final int z) {gridZ = z;}

    public String toKey() {return gridX + "," + gridZ;}


    public void setCoordinate(final int x, final int z)
    {
        gridX = x;
        gridZ = z;
    }
}