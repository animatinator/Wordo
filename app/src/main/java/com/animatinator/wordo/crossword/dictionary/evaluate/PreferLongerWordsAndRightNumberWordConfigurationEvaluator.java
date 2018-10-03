package com.animatinator.wordo.crossword.dictionary.evaluate;

import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

import java.util.List;

/**
 * Maximises average word length and uses squared error to punish configurations with too many or
 * too few words equally.
 */
public class PreferLongerWordsAndRightNumberWordConfigurationEvaluator implements WordConfigurationEvaluator {
    private final int targetWords;

    public PreferLongerWordsAndRightNumberWordConfigurationEvaluator(int targetWords) {
        this.targetWords = targetWords;
    }

    @Override
    public float evaluateWordConfig(PuzzleWordConfiguration config) {
        int numWords = config.getWords().size();

        double divisor = 1.0d + Math.pow(Math.abs(numWords - targetWords), 2);

        return (float)(averageWordLength(config.getWords()) / divisor);
    }

    private double averageWordLength(List<String> words) {
        if (words.size() == 0) {
            return 0;
        }

        int totalLength = words.stream().mapToInt(String::length).sum();
        return ((double)totalLength) / ((double)words.size());
    }
}
