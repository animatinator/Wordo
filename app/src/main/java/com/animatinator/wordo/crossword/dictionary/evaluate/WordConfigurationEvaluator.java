package com.animatinator.wordo.crossword.dictionary.evaluate;

import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

public interface WordConfigurationEvaluator {
    float evaluateWordConfig(PuzzleWordConfiguration config);
}
