package com.animatinator.wordo.crossword.dictionary.evaluate;

import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

/**
 * Prefers {@link PuzzleWordConfiguration}s with more words.
 */
public class MaximisingWordConfigurationEvaluator implements WordConfigurationEvaluator {
    @Override
    public float evaluateWordConfig(PuzzleWordConfiguration config) {
        return config.getWords().size();
    }
}
