package com.animatinator.wordo.crossword.print;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BoardToHumanReadableStringTest {
    private Board testBoard;
    private final String testBoardString = ".t...\n" +
            "hello\n" +
            ".s...\n" +
            ".t...\n";
    private final BoardToHumanReadableString boardToHumanReadableString = new BoardToHumanReadableString();

    @Before
    public void setUpTestBoard() {
        testBoard = new Board();
        testBoard.addWord("test", new BoardPosition(1, 0), Direction.VERTICAL);
        testBoard.addWord("hello", new BoardPosition(0, 1), Direction.HORIZONTAL);
    }

    @Test
    public void printEmptyBoard() {
        Board board = new Board();
        assertEquals(".\n", boardToHumanReadableString.getStringRepresentation(board));
    }

    @Test
    public void printMoreDetailedBoard() {
        assertEquals(testBoardString, boardToHumanReadableString.getStringRepresentation(testBoard));
    }
}
