package com.animatinator.wordo.crossword.generate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BoardGenerationFlagsTest {
    @Test
    public void defaultsSet() {
        BoardGenerationFlags flags = new BoardGenerationFlags();
        assertTrue(flags.getFlag(BoardGenerationFlagConstant.RANDOM_INITIAL_ORIENTATION));
    }

    @Test
    public void unsetFlagIsFalseByDefault() {
        BoardGenerationFlags flags = new BoardGenerationFlags();
        assertFalse(flags.getFlag(BoardGenerationFlagConstant.UNUSED_TEST_FLAG));
    }

    @Test
    public void setFlag() {
        BoardGenerationFlags flags = new BoardGenerationFlags();
        flags.setFlag(BoardGenerationFlagConstant.UNUSED_TEST_FLAG, true);
        assertTrue(flags.getFlag(BoardGenerationFlagConstant.UNUSED_TEST_FLAG));
    }
}
