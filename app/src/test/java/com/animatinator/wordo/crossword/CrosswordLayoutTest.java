package com.animatinator.wordo.crossword;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

        CrosswordLayout layout = new CrosswordLayout(existingBoard, new ProcessedDictionary());

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

        CrosswordLayout layout = new CrosswordLayout(existingBoard, new ProcessedDictionary());

        assertHasValueAtPosition(layout, "h", 0, 3);
        assertHasValueAtPosition(layout, "l", 3, 3);
        assertHasValueAtPosition(layout, "h", 3, 0);
    }

    @Test
    public void getValueAt_emptyBoard() {
        CrosswordLayout layout = new CrosswordLayout(10, 10);
        assertNull(layout.getValueAt(5, 5));
    }

    @Test
    public void getValueAt_addWords() {
        CrosswordLayout layout = new CrosswordLayout(10, 10);

        layout.addWord(new LaidWord("test", new BoardPosition(2, 0), Direction.VERTICAL));
        layout.addWord(new LaidWord("three", new BoardPosition(2, 3), Direction.HORIZONTAL));

        assertHasValueAtPosition(layout, "t", 2, 3);
        assertHasValueAtPosition(layout, "r", 4, 3);
        assertHasValueAtPosition(layout, "e", 2, 1);
    }

    @Test
    public void getValueAt_outOfRange() {
        CrosswordLayout layout = new CrosswordLayout(1, 1);
        try {
            layout.getValueAt(10, 10);
        } catch (IllegalArgumentException expected) {
            return;
        }

        fail("Should throw IllegalArgumentException when argument out of range.");
    }

    @Test
    public void isRevealed_notOnWord() {
        CrosswordLayout layout = new CrosswordLayout(10, 10);
        assertFalse(layout.isRevealed(5, 5));
    }

    @Test
    public void isRevealed_outOfRange() {
        CrosswordLayout layout = new CrosswordLayout(1, 1);
        try {
            layout.isRevealed(10, 10);
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
            assertFalse(layout.isRevealed(0, i));
        }
    }

    @Test
    public void isRevealed_revealed() {
        CrosswordLayout layout = new CrosswordLayout(1, 4);
        layout.addWord(new LaidWord("test", new BoardPosition(0, 0), Direction.VERTICAL));
        layout.maybeRevealWord("test");

        for (int i = 0; i < 4; i++) {
            assertTrue(layout.isRevealed(0, i));
        }
    }

    @Test
    public void isRevealed_wrongWordRevealed() {
        CrosswordLayout layout = new CrosswordLayout(1, 4);
        layout.addWord(new LaidWord("tost", new BoardPosition(0, 0), Direction.VERTICAL));

        for (int i = 0; i < 4; i++) {
            assertFalse(layout.isRevealed(0, i));
        }
    }

    @Test
    public void bonusWords() {
        Board existingBoard = new Board();
        ProcessedDictionary existingDictionary = new ProcessedDictionary();
        existingDictionary.addWordToFullDictionary("test");

        CrosswordLayout layout = new CrosswordLayout(existingBoard, existingDictionary);

        assertFalse(layout.hasBonusWord("testy"));
        assertTrue(layout.hasBonusWord("test"));
    }

    @Test
    public void calculateSize() {
        CrosswordLayout layout = new CrosswordLayout(4, 5);

        Vector2d size = layout.getSize();

        assertEquals(4, size.x());
        assertEquals(5, size.y());
    }

    @Test
    public void calculateSize_zeroByZero() {
        CrosswordLayout zeroByZeroLayout = new CrosswordLayout(0, 0);

        Vector2d size = zeroByZeroLayout.getSize();

        assertEquals(0, size.x());
        assertEquals(0, size.y());
    }

    @Test
    public void calculateSize_zeroByOne() {
        CrosswordLayout zeroByZeroLayout = new CrosswordLayout(0, 1);

        Vector2d size = zeroByZeroLayout.getSize();

        assertEquals(0, size.x());
        assertEquals(1, size.y());
    }

    @Test
    public void calculateSize_OneByZero() {
        CrosswordLayout zeroByZeroLayout = new CrosswordLayout(1, 0);

        Vector2d size = zeroByZeroLayout.getSize();

        // If its height is one, its width has no meaning, so we return zero.
        assertEquals(0, size.x());
        assertEquals(0, size.y());
    }

    @Test
    public void isFinished_zeroByZeroBoard() {
        CrosswordLayout zeroByZero = new CrosswordLayout(0, 0);
        assertTrue(zeroByZero.isFinished());
    }

    @Test
    public void isFinished_emptyBoard() {
        Board emptyBoard = new Board();
        CrosswordLayout emptyLayout = new CrosswordLayout(emptyBoard, new ProcessedDictionary());
        assertTrue(emptyLayout.isFinished());
    }

    @Test
    public void isFinished_notFinished() {
        Board simpleBoard = new Board();
        simpleBoard.addWord("hello", new BoardPosition(5, 6), Direction.HORIZONTAL);
        simpleBoard.addWord("hell", new BoardPosition(8, 3), Direction.VERTICAL);

        CrosswordLayout layout = new CrosswordLayout(simpleBoard, new ProcessedDictionary());
        layout.maybeRevealWord("hello");

        assertFalse(layout.isFinished());
    }

    @Test
    public void isFinished_finished() {
        Board simpleBoard = new Board();
        simpleBoard.addWord("hello", new BoardPosition(5, 6), Direction.HORIZONTAL);
        simpleBoard.addWord("hell", new BoardPosition(8, 3), Direction.VERTICAL);

        CrosswordLayout layout = new CrosswordLayout(simpleBoard, new ProcessedDictionary());
        layout.maybeRevealWord("hello");
        layout.maybeRevealWord("hell");

        assertTrue(layout.isFinished());
    }

    private void assertHasValueAtPosition(
            CrosswordLayout layout, String value, int x, int y) {
        String valueHere = layout.getValueAt(x, y);
        assertNotNull(valueHere);
        assertEquals(value, valueHere);
    }
}