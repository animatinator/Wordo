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
public class BoardToJsonTest {
    private final String testBoardString = "[" +
            "[null, \"t\", null, null, null], " +
            "[\"h\", \"e\", \"l\", \"l\", \"o\"], " +
            "[null, \"s\", null, null, null], " +
            "[null, \"t\", null, null, null]" +
            "]";
    private final String testBoardStringEscaped = "[" +
            "[null, \\\"t\\\", null, null, null], " +
            "[\\\"h\\\", \\\"e\\\", \\\"l\\\", \\\"l\\\", \\\"o\\\"], " +
            "[null, \\\"s\\\", null, null, null], " +
            "[null, \\\"t\\\", null, null, null]" +
            "]";
    private final BoardToJson boardToJson = new BoardToJson();
    private Board testBoard;

    @Before
    public void setUpTestBoard() {
        testBoard = new Board();
        testBoard.addWord("test", new BoardPosition(1, 0), Direction.VERTICAL);
        testBoard.addWord("hello", new BoardPosition(0, 1), Direction.HORIZONTAL);
    }

    @Test
    public void emptyBoard() {
        Board board = new Board();
        assertEquals("[[null]]", boardToJson.getStringRepresentation(board));
    }

    @Test
    public void moreDetailedBoard() {
        assertEquals(testBoardString, boardToJson.getStringRepresentation(testBoard));
    }

    @Test
    public void moreDetailedBoard_escaped() {
        BoardToJson escapingBoardToJson = new BoardToJson(true);
        assertEquals(testBoardStringEscaped, escapingBoardToJson.getStringRepresentation(testBoard));
    }
}
