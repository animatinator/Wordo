package com.animatinator.wordo.crossword.util;

public class BoardOffset extends Vector2d {
    public BoardOffset(int x, int y) {
        super(x, y);
    }

    public BoardOffset(Vector2d other) {
        super(other);
    }

    @Override
    public BoardOffset negative() {
        return new BoardOffset(super.negative());
    }

    @Override
    public BoardOffset multiply(int factor) {
        return new BoardOffset(super.multiply(factor));
    }
}
