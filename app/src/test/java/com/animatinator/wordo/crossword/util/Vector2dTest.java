package com.animatinator.wordo.crossword.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class Vector2dTest {
    @Test
    public void negative() {
        Vector2d position = new Vector2d(1, 2);
        assertEquals(new Vector2d(-1, -2), position.negative());
    }

    @Test
    public void multiply() {
        Vector2d position = new Vector2d(1, 2);
        assertEquals(new Vector2d(3, 6), position.multiply(3));
    }

    @Test
    public void immutable() {
        Vector2d position = new Vector2d(1, 2);
        position.multiply(3);
        assertEquals(1, position.x());
        assertEquals(2, position.y());
    }
}
