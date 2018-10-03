package com.animatinator.wordo.game;

import com.animatinator.wordo.crossword.CrosswordLayout;
import com.animatinator.wordo.crossword.PuzzleConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.security.spec.PSSParameterSpec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class PuzzleStoreTest {
    private final PuzzleConfiguration emptyPuzzleConfig =
            new PuzzleConfiguration(new String[]{}, new CrosswordLayout(1, 1));

    @Test
    public void addItemAndRetrieve() {
        long id = PuzzleStore.storePuzzleConfiguration(emptyPuzzleConfig);
        assertEquals(emptyPuzzleConfig, PuzzleStore.loadPuzzleConfiguration(id));
    }

    @Test
    public void storedIdsIncrease() {
        long firstId = PuzzleStore.storePuzzleConfiguration(emptyPuzzleConfig);
        long secondId = PuzzleStore.storePuzzleConfiguration(emptyPuzzleConfig);
        assertNotEquals(firstId, secondId);
    }

    @Test
    public void getNonExistentId() {
        assertNull(PuzzleStore.loadPuzzleConfiguration(-100));
    }
}