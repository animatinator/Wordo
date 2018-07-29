package com.animatinator.wordo.crossword.util;

import com.animatinator.wordo.crossword.board.BoardLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BoardLayoutUtilsTest {
    @Test
    public void countIntersections_emptyBoard() {
        assertEquals(0, BoardLayoutUtils.countIntersections(new BoardLayout(1, 1)));
    }

    @Test
    public void countIntersections_simpleCross() {
        BoardLayout layout = new BoardLayout(4, 4);
        layout.copyLayoutFromStringArray(new String[][]{
                "..a..".split(""),
                ".ace.".split(""),
                "..e..".split("")
        });
        assertEquals(1, BoardLayoutUtils.countIntersections(layout));
    }

    @Test
    public void countIntersections_tJunction() {
        BoardLayout layout = new BoardLayout(4, 4);
        layout.copyLayoutFromStringArray(new String[][]{
                "..a.e".split(""),
                "paces".split(""),
                "..e.t".split("")
        });
        assertEquals(2, BoardLayoutUtils.countIntersections(layout));
    }

    @Test
    public void countIntersections_corner() {
        BoardLayout layout = new BoardLayout(4, 4);
        layout.copyLayoutFromStringArray(new String[][]{
                "....s".split(""),
                "....a".split(""),
                ".test".split("")
        });
        assertEquals(1, BoardLayoutUtils.countIntersections(layout));
    }

    @Test
    public void countIntersections_offset() {
        BoardLayout layout = new BoardLayout(4, 4, new BoardPosition(-100, -100));
        layout.copyLayoutFromStringArray(new String[][]{
                ".test".split(""),
                "....e".split(""),
                "..sea".split("")
        });
        assertEquals(2, BoardLayoutUtils.countIntersections(layout));
    }
}
