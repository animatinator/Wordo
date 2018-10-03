package com.animatinator.wordo.crossword.testutils;

import com.animatinator.wordo.crossword.ProgressCallback;

import static org.junit.Assert.assertTrue;

/**
 * Listens to progress and asserts that it increases monotonically, then gives access to the
 * final value.
 */
public final class TestProgressListener implements ProgressCallback {
    private double curProgress = 0.0d;

    @Override
    public void setProgress(double progress) {
        assertTrue(progress >= curProgress);
        curProgress = progress;
    }

    public double getProgress() {
        return curProgress;
    }
}
