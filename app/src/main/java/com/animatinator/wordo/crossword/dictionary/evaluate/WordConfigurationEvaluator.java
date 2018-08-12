package com.animatinator.wordo.crossword.dictionary.evaluate;

import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

/**
 * Evaluates {@link PuzzleWordConfiguration}s for the puzzle generator.
 */
public interface WordConfigurationEvaluator {
    float evaluateWordConfig(PuzzleWordConfiguration config);
}
