package api;

import static api.HexagonOrientation.FLAT_TOP;

public final class CoordinateConverter {

    public CoordinateConverter() {
        throw new UnsupportedOperationException("This utility class is not meant to be instantiated.");
    }

    public static int convertOffsetCoordinatesToAxialX(final int offsetX, final int offsetY, final HexagonOrientation orientation) {
        return FLAT_TOP.equals(orientation) ? offsetX : offsetX - offsetY / 2;
    }

    public static int convertOffsetCoordinatesToAxialZ(final int offsetX, final int offsetY, final HexagonOrientation orientation) {
        return FLAT_TOP.equals(orientation) ? offsetY - offsetX / 2 : offsetY;
    }

    public static int convertToCol(final int AxialX, final int AxialZ, final HexagonOrientation orientation) {
        return  AxialX + (AxialZ - (AxialZ&1)) / 2;
    }

    public static int convertToRow(final int AxialZ) {
        return  AxialZ;
    }


}

