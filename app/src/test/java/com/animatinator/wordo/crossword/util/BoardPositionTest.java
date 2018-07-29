package com.animatinator.wordo.crossword.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BoardPositionTest {
    @Test
    public void addXOffset() {
        BoardPosition position = new BoardPosition(1, 2);
        BoardPosition adjusted = position.withXOffset(3);
        assertEquals(4, adjusted.x());
    }

    @Test
    public void addYOffset() {
        BoardPosition position = new BoardPosition(1, 2);
        BoardPosition adjusted = position.withYOffset(3);
        assertEquals(5, adjusted.y());
    }

    @Test
    public void addOffset() {
        BoardPosition position = new BoardPosition(1, 2);
        BoardPosition adjusted = position.withOffset(new BoardOffset(3, 4));
        assertEquals(new BoardPosition(4, 6), adjusted);
    }

    @Test
    public void negative() {
        BoardPosition position = new BoardPosition(1, 2);
        assertEquals(new BoardPosition(-1, -2), position.negative());
    }

    @Test
    public void multiply() {
        BoardPosition position = new BoardPosition(1, 2);
        assertEquals(new BoardPosition(3, 6), position.multiply(3));
    }

    @Test
    public void immutable() {
        BoardPosition position = new BoardPosition(1, 2);
        position.withXOffset(3);
        position.withYOffset(3);
        assertEquals(1, position.x());
        assertEquals(2, position.y());
    }
}
