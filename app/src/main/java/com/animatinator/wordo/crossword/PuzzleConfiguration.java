package com.animatinator.wordo.crossword;

public class PuzzleConfiguration {
    private final String[] letters;
    private final CrosswordLayout layout;

    public PuzzleConfiguration(String[] letters, CrosswordLayout layout) {
        this.letters = letters;
        this.layout = layout;
    }

    public String[] getLetters() {
        return letters;
    }

    public CrosswordLayout getLayout() {
        return layout;
    }
}
