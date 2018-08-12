package com.animatinator.wordo.crossword.dictionary.evaluate;

import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

/**
 * Prefers {@link PuzzleWordConfiguration}s which have close to the intended number of words.
 */
public class OptimisingWordConfigurationEvaluator implements WordConfigurationEvaluator {
    // The 'ideal' ratio of number of words possible in the config to number of words we want to put
    // in the puzzle. Greater than one because a few bonus words won't hurt, and we want to have
    // options in generating the board layout.
    private static final float TARGET_WORD_RATIO = 1.2f;

    // The number of words we want to put in the puzzle.
    private final int intendedNumberOfWords;

    public OptimisingWordConfigurationEvaluator(int intendedNumberOfWords) {
        this.intendedNumberOfWords = intendedNumberOfWords;
    }

    @Override
    public float evaluateWordConfig(PuzzleWordConfiguration config) {
        float numWords = config.getWords().size();

        float targetWords = TARGET_WORD_RATIO * intendedNumberOfWords;

        if (numWords < targetWords) {
            float ratio = numWords / targetWords;
            // Squared because shortfalls should be heavily punished - we don't want to generate
            // tiny boards.
            return ratio * ratio;
        } else {
            // Scale the score down linearly as we increase above targetWords. Double the target
            // means a score of 0.5.
            // This is linear because having too many words isn't nearly as bad as having too few;
            // we just don't want vast numbers of words.
            return targetWords / numWords;
        }
    }
}
