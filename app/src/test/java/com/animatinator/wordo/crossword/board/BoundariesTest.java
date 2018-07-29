package com.animatinator.wordo.crossword.board;

import com.animatinator.wordo.crossword.util.BoardPosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class BoundariesTest {
    @Test
    public void invalidWidth() {
        try {
            new Boundaries(new BoardPosition(2, 3), new BoardPosition(1, 4));
        } catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void invalidHeight() {
        try {
            new Boundaries(new BoardPosition(2, 3), new BoardPosition(3, 2));
        } catch(IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void width() {
        Boundaries boundaries = new Boundaries(new BoardPosition(0, 0), new BoardPosition(3, 4));
        assertEquals(4, boundaries.getWidth());
    }

    @Test
    public void height() {
        Boundaries boundaries = new Boundaries(new BoardPosition(0, 0), new BoardPosition(3, 4));
        assertEquals(5, boundaries.getHeight());
    }

    @Test
    public void point() {
        Boundaries boundaries = new Boundaries(new BoardPosition(3, 4), new BoardPosition(3, 4));
        assertEquals(1, boundaries.getWidth());
        assertEquals(1, boundaries.getHeight());
    }
}
