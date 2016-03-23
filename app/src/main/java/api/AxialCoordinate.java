package api;


import static java.lang.Integer.parseInt;


public final class AxialCoordinate  {

    private final int gridX;
    private final int gridZ;

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

    public int getGridX()
    {
        return gridX;
    }

    public int getGridZ()
    {
        return gridZ;
    }

    public static AxialCoordinate fromCoordinates(final int gridX, final int gridZ) {
        return new AxialCoordinate(gridX, gridZ);
    }

    public String toKey() {
        return gridX + "," + gridZ;
    }

}
