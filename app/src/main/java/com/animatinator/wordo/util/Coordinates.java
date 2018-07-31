package com.animatinator.wordo.util;

public class Coordinates {
    private final float x;
    private final float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public Coordinates withXOffset(float offset) {
        return new Coordinates(x + offset, y);
    }

    public Coordinates withYOffset(float offset) {
        return new Coordinates(x, y + offset);
    }
}
