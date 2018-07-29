package com.animatinator.wordo.crossword.dictionary.puzzle;

import java.util.List;

public class PuzzleConfiguration {
    private final String[] letters;
    private final List<String> words;
    private final int numberOfLettersRequired;

    PuzzleConfiguration(String[] letters, List<String> words, int numberOfLettersRequired) {
        this.letters = letters;
        this.words = words;
        this.numberOfLettersRequired = numberOfLettersRequired;
    }

    String[] getLetters() {
        return letters;
    }

    List<String> getWords() {
        return words;
    }

    int getNumberOfLettersRequired() {
        return numberOfLettersRequired;
    }

    @Override
    public String toString() {
        return "PuzzleConfiguration{" +
                "letter set=" + letterString() +
                "; words=" + words +
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
