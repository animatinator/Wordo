package com.animatinator.wordo.crossword;

import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

public class PuzzleConfiguration {
    private final PuzzleWordConfiguration wordConfiguration;
    private final CrosswordLayout layout;

    public PuzzleConfiguration(PuzzleWordConfiguration wordConfiguration, CrosswordLayout layout) {
        this.wordConfiguration = wordConfiguration;
        this.layout = layout;
    }

    public PuzzleWordConfiguration getWordConfiguration() {
        return wordConfiguration;
    }

    public CrosswordLayout getLayout() {
        return layout;
    }
}
