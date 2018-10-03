package com.animatinator.wordo.crossword;

/**
 * Used by the {@link PuzzleGenerator} to report on its progress.
 */
public interface PuzzleGenerationProgressCallback extends ProgressCallback {

    /**
     * Set a user-visible string name for the work currently being done.
     */
    void setGenerationState(String state);
}
