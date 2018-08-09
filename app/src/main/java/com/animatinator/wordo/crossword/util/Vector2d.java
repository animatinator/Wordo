package com.animatinator.wordo.crossword.util;

import java.util.Locale;
import java.util.Objects;

public class Vector2d {
    protected final int x;
    protected final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d other) {
        this.x = other.x();
        this.y = other.y();
    }

    public int x() {
        return  x;
    }

    public int y() {
        return y;
    }

    public Vector2d negative() {
        return new Vector2d(-x, -y);
    }

    public Vector2d multiply(int factor) {
        return new Vector2d(x * factor, y * factor);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Vector2d(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Vector2d)) return false;

        Vector2d otherPosition = (Vector2d) other;
        return otherPosition.x == x && otherPosition.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
