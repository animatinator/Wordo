package com.animatinator.wordo.crossword;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;
import com.animatinator.wordo.crossword.util.Vector2d;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class CrosswordLayoutTest {
    @Test
    public void construction() {
        CrosswordLayout layout = new CrosswordLayout(3, 4);
        assertEquals(new Vector2d(3, 4), layout.getSize());
    }

    @Test
    public void constructionFromExistingBoard_laidWordsCorrect() {
        Board existingBoard = new Board();
        existingBoard.addWord("hello", new BoardPosition(5, 6), Direction.HORIZONTAL);
        existingBoard.addWord("hell", new BoardPosition(8, 3), Direction.VERTICAL);

        CrosswordLayout layout = new CrosswordLayout(existingBoard);

        Map<String, LaidWord> laidWordMap = layout.getLaidWordsMapForTesting();
        assertEquals(
                new LaidWord("hello", new BoardPosition(0, 3), Direction.HORIZONTAL),
                laidWordMap.get("hello"));
        assertEquals(
                new LaidWord("hell", new BoardPosition(3, 0), Direction.VERTICAL),
                laidWordMap.get("hell"));
    }

    /**
     * The layout tested here has a couple of words intersecting at (8, 6). This layout will end up
     * having an offset because the BoardLayout generated from it will not have its top-left at
     * (0, 0). As a result, this test both verifies that we copy layouts correctly and that it
     * handles offsets correctly.
     */
    @Test
    public void constructionFromExistingBoard_copiedLayoutCorrect() {
        Board existingBoard = new Board();
        existingBoard.addWord("hello", new BoardPosition(5, 6), Direction.HORIZONTAL);
        existingBoard.addWord("hell", new BoardPosition(8, 3), Direction.VERTICAL);

        CrosswordLayout layout = new CrosswordLayout(existingBoard);

        assertHasValueAtPosition(layout, "h", new BoardPosition(0, 3));
        assertHasValueAtPosition(layout, "l", new BoardPosition(3, 3));
        assertHasValueAtPosition(layout, "h", new BoardPosition(3, 0));
    }

    @Test
    public void getValueAt_emptyBoard() {
        CrosswordLayout layout = new CrosswordLayout(10, 10);
        assertFalse(layout.getValueAt(new BoardPosition(5, 5)).isPresent());
    }

    @Test
    public void getValueAt_addWords() {
        CrosswordLayout layout = new CrosswordLayout(10, 10);

        layout.addWord(new LaidWord("test", new BoardPosition(2, 0), Direction.VERTICAL));
        layout.addWord(new LaidWord("three", new BoardPosition(2, 3), Direction.HORIZONTAL));

        assertHasValueAtPosition(layout, "t", new BoardPosition(2, 3));
        assertHasValueAtPosition(layout, "r", new BoardPosition(4, 3));
        assertHasValueAtPosition(layout, "e", new BoardPosition(2, 1));
    }

    @Test
    public void getValueAt_outOfRange() {
        CrosswordLayout layout = new CrosswordLayout(1, 1);
        try {
            layout.getValueAt(new BoardPosition(10, 10));
        } catch (IllegalArgumentException expected) {
            return;
        }

        fail("Should throw IllegalArgumentException when argument out of range.");
    }

    @Test
    public void isRevealed_notOnWord() {
        CrosswordLayout layout = new CrosswordLayout(10, 10);
        assertFalse(layout.isRevealed(new BoardPosition(5, 5)));
    }

    @Test
    public void isRevealed_outOfRange() {
        CrosswordLayout layout = new CrosswordLayout(1, 1);
        try {
            layout.isRevealed(new BoardPosition(10, 10));
        } catch (IllegalArgumentException expected) {
            return;
        }

        fail("Should throw IllegalArgumentException when argument out of range.");
    }

    @Test
    public void isRevealed_unrevealed() {
        CrosswordLayout layout = new CrosswordLayout(1, 4);
        layout.addWord(new LaidWord("test", new BoardPosition(0, 0), Direction.VERTICAL));

        for (int i = 0; i < 4; i++) {
            assertFalse(layout.isRevealed(new BoardPosition(0, i)));
        }
    }

    @Test
    public void isRevealed_revealed() {
        CrosswordLayout layout = new CrosswordLayout(1, 4);
        layout.addWord(new LaidWord("test", new BoardPosition(0, 0), Direction.VERTICAL));
        layout.maybeRevealWord("test");

        for (int i = 0; i < 4; i++) {
            assertTrue(layout.isRevealed(new BoardPosition(0, i)));
        }
    }

    @Test
    public void isRevealed_wrongWordRevealed() {
        CrosswordLayout layout = new CrosswordLayout(1, 4);
        layout.addWord(new LaidWord("tost", new BoardPosition(0, 0), Direction.VERTICAL));

        for (int i = 0; i < 4; i++) {
            assertFalse(layout.isRevealed(new BoardPosition(0, i)));
        }
    }

    private void assertHasValueAtPosition(
            CrosswordLayout layout, String value, BoardPosition position) {
        Optional<String> valueHere = layout.getValueAt(position);
        assertTrue(valueHere.isPresent());
        assertEquals(value, valueHere.get());
    }
}