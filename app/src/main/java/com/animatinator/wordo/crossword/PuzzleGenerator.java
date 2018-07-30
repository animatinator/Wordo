package com.animatinator.wordo.crossword;

import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;
import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;
import com.animatinator.wordo.crossword.dictionary.puzzle.WordConfigurationGenerator;

public class PuzzleGenerator {
    public PuzzleGenerator() {

    }

    public PuzzleConfiguration createPuzzle(PuzzleGenerationSettings generationSettings) {
        ProcessedDictionary dictionary = loadDictionary();
        PuzzleWordConfiguration wordConfiguration =
                generateWordConfiguration(generationSettings, dictionary);
        return new PuzzleConfiguration(wordConfiguration, null /* TODO: CrosswordLayout */);
    }

    private PuzzleWordConfiguration generateWordConfiguration(
            PuzzleGenerationSettings generationSettings, ProcessedDictionary dictionary) {
        WordConfigurationGenerator wordConfigGenerator =
                new WordConfigurationGenerator(dictionary)
                        .withMaximumWordCount(generationSettings.getMaxWords())
                        .withMinimumWordLength(generationSettings.getMinWordLength());
        return wordConfigGenerator.buildPuzzle(generationSettings.getNumLetters());
    }

    private ProcessedDictionary loadDictionary() {
        // TODO: Load dictionary.
        return new ProcessedDictionary();
    }
}
