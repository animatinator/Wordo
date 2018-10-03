package com.animatinator.wordo.crossword;

public interface ProgressCallback {
    /**
     * Set the progress to a value between 0.0d and 1.0d.
     */
    void setProgress(double progress);
}
