package api;


public enum HexagonOrientation {

    POINTY_TOP(0.5f), FLAT_TOP(0);

    private float coordinateOffset;

    HexagonOrientation(float coordinateOffset) {
        this.coordinateOffset = coordinateOffset;
    }

    public final float getCoordinateOffset() {
        return coordinateOffset;
    }

}
