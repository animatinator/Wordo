package com.animatinator.wordo.crossword.board;

import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BoardTest {
    private Board simpleBoard;
    private BoardLayout simpleBoardLayout;

    private Board negativeBoard;
    private BoardLayout negativeBoardLayout;

    /**
     * test..
     * ...i..
     * words.
     * ...y..
     */
    @Before
    public void setUpSimpleBoard() {
        simpleBoard = new Board();
        simpleBoard.addWord("test", new BoardPosition(0, 0), Direction.HORIZONTAL);
        simpleBoard.addWord("words", new BoardPosition(0, 2), Direction.HORIZONTAL);
        simpleBoard.addWord("tidy", new BoardPosition(3, 0), Direction.VERTICAL);

        simpleBoardLayout = new BoardLayout(5, 4);
        simpleBoardLayout.copyLayoutFromStringArray(new String[][]{
                {"t", "e", "s", "t", "."},
                {".", ".", ".", "i", "."},
                {"w", "o", "r", "d", "s"},
                {".", ".", ".", "y", "."},});
    }

    /**
     * This board is just the word 'negativity' spelt horizontally, but its top-left corner is in a negative position.
     */
    @Before
    public void setUpNegativeBoard() {
        negativeBoard = new Board();
        negativeBoard.addWord("negativity", new BoardPosition(-1, -1), Direction.HORIZONTAL);

        negativeBoardLayout = new BoardLayout(10, 1, new BoardPosition(-1, -1));
        negativeBoardLayout.copyLayoutFromStringArray(new String[][]{"negativity".split("")});
    }

    @Test
    public void construction() {
        Board board = new Board();
    }

    @Test
    public void emptyLayout() {
        Board board = new Board();
        BoardLayout layout = board.getLayout();
        assertEquals(1, layout.getWidth());
        assertEquals(1, layout.getHeight());
    }

    @Test
    public void emptyBoundaries() {
        Board board = new Board();
        Boundaries boundaries = board.getBoundaries();

        assertEquals(new BoardPosition(0, 0), boundaries.getTopLeft());
        assertEquals(new BoardPosition(0, 0), boundaries.getBottomRight());
    }

    @Test
    public void simpleBoundaries() {
        Board board = new Board();
        board.addWord("test", new BoardPosition(0, 0), Direction.HORIZONTAL);

        Boundaries boundaries = board.getBoundaries();
        assertEquals(new BoardPosition(0, 0), boundaries.getTopLeft());
        assertEquals(new BoardPosition(3, 0), boundaries.getBottomRight());
    }

    @Test
    public void boundariesFromMultipleWords() {
        Boundaries boundaries = simpleBoard.getBoundaries();
        assertEquals(new BoardPosition(0, 0), boundaries.getTopLeft());
        assertEquals(new BoardPosition(4, 3), boundaries.getBottomRight());
    }

    @Test
    public void negativeBoundaries() {
        Board board = new Board();
        board.addWord("test", new BoardPosition(-5, -5), Direction.HORIZONTAL);

        Boundaries boundaries = board.getBoundaries();
        assertEquals(new BoardPosition(-5, -5), boundaries.getTopLeft());
        assertEquals(new BoardPosition(-2, -5), boundaries.getBottomRight());
    }

    @Test
    public void wordLaidCorrectly_vertical() {
        Board board = new Board();
        board.addWord("word", new BoardPosition(3, 4), Direction.VERTICAL);
        BoardLayout layout = board.getLayout();
        assertValueAtPositionEquals(layout, 3, 4, "w");
        assertValueAtPositionEquals(layout, 3, 5, "o");
        assertValueAtPositionEquals(layout, 3, 6, "r");
        assertValueAtPositionEquals(layout, 3, 7, "d");
    }

    @Test
    public void wordLaidCorrectly_horizontal() {
        Board board = new Board();
        board.addWord("word", new BoardPosition(3, 4), Direction.HORIZONTAL);
        BoardLayout layout = board.getLayout();
        assertValueAtPositionEquals(layout, 3, 4, "w");
        assertValueAtPositionEquals(layout, 4, 4, "o");
        assertValueAtPositionEquals(layout, 5, 4, "r");
        assertValueAtPositionEquals(layout, 6, 4, "d");
    }

    @Test
    public void badIntersectionDenied() {
        Board board = new Board();
        board.addWord("patter", new BoardPosition(1, 2), Direction.HORIZONTAL);
        try {
            board.addWord("test", new BoardPosition(2, 2), Direction.VERTICAL);
        } catch (IllegalArgumentException expected) {
            return;
        }
        fail("Shouldn't have been able to add 'test' there");
    }

    @Test
    public void getLayout() {
        assertEquals(simpleBoardLayout, simpleBoard.getLayout());
    }

    @Test
    public void getLayout_negativePositions() {
        assertEquals(negativeBoardLayout, negativeBoard.getLayout());
    }

    @Test
    public void getAttachments_simple() {
        Board board = new Board();
        board.addWord("patter", new BoardPosition(1, 2), Direction.HORIZONTAL);

        List<LaidWord> expected = new ArrayList<>();
        expected.add(new LaidWord("test", new BoardPosition(3, 2), Direction.VERTICAL));
        expected.add(new LaidWord("test", new BoardPosition(4, 2), Direction.VERTICAL));
        expected.add(new LaidWord("test", new BoardPosition(5, 1), Direction.VERTICAL));

        List<LaidWord> attachments = board.getPossibleAttachmentPointsForWord("test");
        assertListsEqual(expected, attachments);
    }

    @Test
    public void getAttachments_obstacle() {
        Board board = new Board();
        board.addWord("batting", new BoardPosition(1, 2), Direction.HORIZONTAL);
        board.addWord("xxxxxxx", new BoardPosition(1, 4), Direction.HORIZONTAL);

        List<LaidWord> attachments = board.getPossibleAttachmentPointsForWord("test");
        assertEquals(0, attachments.size());
    }

    @Test
    public void getAttachments_validUnintendedOverlap() {
        Board board = new Board();
        board.addWord("patter", new BoardPosition(1, 2), Direction.HORIZONTAL);
        board.addWord("sat", new BoardPosition(1, 5), Direction.HORIZONTAL);

        List<LaidWord> expected = new ArrayList<>();
        expected.add(new LaidWord("test", new BoardPosition(5, 1), Direction.VERTICAL));
        expected.add(new LaidWord("test", new BoardPosition(3, 5), Direction.VERTICAL));
        // This is the interesting one: the proposed attachment with 'patter' also intersects with 'sat', but in a legal
        // way.
        expected.add(new LaidWord("test", new BoardPosition(3, 2), Direction.VERTICAL));

        List<LaidWord> attachments = board.getPossibleAttachmentPointsForWord("test");
        assertListsEqual(expected, attachments);
    }

    // TODO: Cleaner testing that we can't add words adjacent to existing ones.

    @Test
    public void nonTrivialIntersectionCase_shouldNotSuggestAttachment() {
        Board board = new Board();
        board.addWord("caused", new BoardPosition(0, 0), Direction.HORIZONTAL);
        board.addWord("cause", new BoardPosition(0, 0), Direction.VERTICAL);
        board.addWord("sauce", new BoardPosition(2, -2), Direction.VERTICAL);

        List<LaidWord> attachments = board.getPossibleAttachmentPointsForWord("aces");
        assertFalse(attachments.contains(new LaidWord("aces", new BoardPosition(1, 0), Direction.VERTICAL)));
    }

    @Test
    public void shouldNotSuggestAttachmentContinuingExistingWord() {
        Board board = new Board();
        board.addWord("sea", new BoardPosition(0, 0), Direction.HORIZONTAL);
        board.addWord("amaze", new BoardPosition(2, 0), Direction.VERTICAL);

        List<LaidWord> attachments = board.getPossibleAttachmentPointsForWord("sea");
        assertFalse(attachments.contains(new LaidWord("sea", new BoardPosition(2, 0), Direction.HORIZONTAL)));
    }

    @Test
    public void shouldNotSuggestWordAdjacentToExistingOne() {
        // Simulate the below case (capital letters are the word attachment which shouldn't be suggested):
        // ..c...
        // ..a...
        // caseS.
        // ..e.E.
        // ..seA.
        Board board = new Board();
        board.addWord("case", new BoardPosition(0, 2), Direction.HORIZONTAL);
        board.addWord("cases", new BoardPosition(2, 0), Direction.VERTICAL);
        board.addWord("sea", new BoardPosition(2, 4), Direction.HORIZONTAL);

        List<LaidWord> attachments = board.getPossibleAttachmentPointsForWord("sea");
        assertFalse(attachments.contains(new LaidWord("sea", new BoardPosition(4, 2), Direction.VERTICAL)));
    }

    private void assertValueAtPositionEquals(BoardLayout layout, int x, int y, String expectedValue) {
        Optional<String> optionalValue = layout.getAt(new BoardPosition(x, y));
        assertTrue(optionalValue.isPresent());
        assertEquals(expectedValue, optionalValue.get());
    }

    private <T> void assertListsEqual(List<T> first, List<T> second) {
        assertEquals(first.size(), second.size());
        assertTrue(first.containsAll(second));
        assertTrue(second.containsAll(first));
    }
}
