package com.animatinator.wordo.crossword.dictionary.puzzle;

import java.util.List;

public class PuzzleWordConfiguration {
    private final String[] letters;
    private final List<String> words;
    private final List<String> bonusWords;
    private final int numberOfLettersRequired;

    PuzzleWordConfiguration(
            String[] letters,
            List<String> words,
            List<String> bonusWords,
            int numberOfLettersRequired) {
        this.letters = letters;
        this.words = words;
        this.bonusWords = bonusWords;
        this.numberOfLettersRequired = numberOfLettersRequired;
    }

    public String[] getLetters() {
        return letters;
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> getBonusWords() {
        return bonusWords;
    }

    int getNumberOfLettersRequired() {
        return numberOfLettersRequired;
    }

    @Override
    public String toString() {
        return "PuzzleWordConfiguration{" +
                "letter set=" + letterString() +
                "; words=" + words +
                "; bonus words=" + bonusWords +
                '}';
    }

    private String letterString() {
        StringBuilder builder = new StringBuilder("{ ");
        for (String letter : letters) {
            builder.append(letter);
            builder.append(", ");
        }
        builder.append("}");

        return builder.toString();
    }
}
