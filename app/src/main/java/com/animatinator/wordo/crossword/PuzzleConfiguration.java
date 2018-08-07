package com.animatinator.wordo.crossword;

import java.util.List;

public class PuzzleConfiguration {
    private final String[] letters;
    private final CrosswordLayout layout;
    private final List<String> bonusWords;

    public PuzzleConfiguration(String[] letters, CrosswordLayout layout, List<String> bonusWords) {
        this.letters = letters;
        this.layout = layout;
        this.bonusWords = bonusWords;
    }

    public String[] getLetters() {
        return letters;
    }

    public CrosswordLayout getLayout() {
        return layout;
    }

    public List<String> getBonusWords() {
        return bonusWords;
    }
}
