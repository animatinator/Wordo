package com.animatinator.wordo.crossword.board.words;

import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LaidWordTest {
    @Test
    public void testGetChars() {
        LaidWord word = new LaidWord("test", new BoardPosition(0, 0), Direction.HORIZONTAL);
        assertTrue(Arrays.equals(new String[] {"t", "e", "s", "t"}, word.getCharacters().toArray()));
    }
}