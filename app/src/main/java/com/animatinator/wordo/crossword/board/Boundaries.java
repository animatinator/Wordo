package com.animatinator.wordo.crossword.board;

import com.animatinator.wordo.crossword.util.BoardPosition;

import java.util.Objects;

public class Boundaries {
    private final BoardPosition topLeft;
    private final BoardPosition bottomRight;

    Boundaries(BoardPosition topLeft, BoardPosition bottomRight) {
        if (topLeft.x() > bottomRight.x()) {
            throw new IllegalArgumentException("Top left boundary cannot be right of bottom right!");
        }
        if (topLeft.y() > bottomRight.y()) {
            throw new IllegalArgumentException("Top left boundary cannot be below bottom right!");
        }

        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    BoardPosition getTopLeft() {
        return topLeft;
    }

    BoardPosition getBottomRight() {
        return bottomRight;
    }

    int getWidth() {
        return (bottomRight.x() + 1) - topLeft.x();
    }

    int getHeight() {
        return (bottomRight.y() + 1) - topLeft.y();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Boundaries)) return false;
        Boundaries otherBoundaries = (Boundaries) other;

        return otherBoundaries.getTopLeft().equals(getTopLeft())
                && otherBoundaries.getBottomRight().equals(getBottomRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(topLeft, bottomRight);
    }
}
