package com.animatinator.wordo.crossword.board;

import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Vector2d;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class PositionAdjustedBoardPositionTest {
    @Test
    public void positionAdjustsCorrectly() {
        BoardPosition basePosition = new BoardPosition(1, 2);
        BoardPosition offset = new BoardPosition(-10, -10);
        BoardLayout.PositionAdjustedBoardPosition adjustedPosition =
                new BoardLayout.PositionAdjustedBoardPosition(basePosition, offset);
        assertEquals(new Vector2d(11, 12), adjustedPosition);
    }
}
