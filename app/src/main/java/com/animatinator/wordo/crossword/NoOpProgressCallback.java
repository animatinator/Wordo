package com.animatinator.wordo.crossword;

/**
 * Does nothing. Used when we want to use a progress-reporting method without caring about its
 * progress.
 */
public class NoOpProgressCallback implements ProgressCallback {
    @Override
    public void setProgress(double progress) {
        // Do nothing.
    }
}
