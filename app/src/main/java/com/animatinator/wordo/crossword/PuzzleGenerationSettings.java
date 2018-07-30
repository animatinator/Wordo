package com.animatinator.wordo.crossword;

/**
 * Stores the settings to use in generating a puzzle instance.
 */
public class PuzzleGenerationSettings {
    private static final int DEFAULT_MAX_WORDS = 10;
    private static final int DEFAULT_MIN_WORD_LENGTH = 3;
    private static final int DEFAULT_NUM_LETTERS = 6;

    private final int maxWords;
    private final int minWordLength;
    private final int numLetters;

    public PuzzleGenerationSettings() {
        this(DEFAULT_MAX_WORDS, DEFAULT_MIN_WORD_LENGTH, DEFAULT_NUM_LETTERS);
    }

    private PuzzleGenerationSettings(int maxWords, int minWordLength, int numLetters) {
        this.maxWords = maxWords;
        this.minWordLength = minWordLength;
        this.numLetters = numLetters;
    }

    public PuzzleGenerationSettings withMaxWords(int maxWords) {
        return new PuzzleGenerationSettings(maxWords, minWordLength, numLetters);
    }

    public PuzzleGenerationSettings withMinWordLength(int minWordLength) {
        return new PuzzleGenerationSettings(maxWords, minWordLength, numLetters);
    }

    public PuzzleGenerationSettings withNumLetters(int numLetters) {
        return new PuzzleGenerationSettings(maxWords, minWordLength, numLetters);
    }

    public int getMaxWords() {
        return maxWords;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public int getNumLetters() {
        return numLetters;
    }
}
