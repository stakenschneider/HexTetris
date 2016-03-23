package api;



public final class CoordinateConverter {

    public CoordinateConverter() {
        throw new UnsupportedOperationException("This utility class is not meant to be instantiated.");
    }

    public static int convertOffsetCoordinatesToAxialX(final int offsetX, final int offsetY) {
        return  offsetX - offsetY / 2;
    }

    public static int convertOffsetCoordinatesToAxialZ(final int offsetX, final int offsetY) {
        return  offsetY;
    }

    public static int convertToCol(final int AxialX, final int AxialZ) {
        return  AxialX + (AxialZ - (AxialZ&1)) / 2;
    }

    public static int convertToRow(final int AxialZ) {
        return  AxialZ;
    }


}

