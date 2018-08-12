package com.animatinator.wordo.crossword;

import android.util.Log;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.dictionary.evaluate.MaximisingWordConfigurationEvaluator;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;
import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;
import com.animatinator.wordo.crossword.dictionary.puzzle.WordConfigurationGenerator;
import com.animatinator.wordo.crossword.evaluate.BoardEvaluator;
import com.animatinator.wordo.crossword.evaluate.SimpleBoardEvaluator;
import com.animatinator.wordo.crossword.generate.BoardGenerationFlagConstant;
import com.animatinator.wordo.crossword.generate.BoardGenerationFlags;
import com.animatinator.wordo.crossword.generate.BoardGenerator;

// TODO: Test this.
public class PuzzleGenerator {
    private static final String TAG = "PuzzleGenerator";

    private final ProcessedDictionary dictionary;

    public PuzzleGenerator(ProcessedDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public PuzzleConfiguration createPuzzle(PuzzleGenerationSettings generationSettings) {
        Log.d(TAG, "Generating word config...");
        PuzzleWordConfiguration wordConfiguration =
                generateWordConfiguration(generationSettings, dictionary);
        Log.d(TAG, "Word config: "+wordConfiguration);
        Log.d(TAG, "Generating layout...");
        CrosswordLayout crosswordLayout = generateLayout(wordConfiguration, dictionary);
        Log.d(TAG, "Layout generated.");

        return new PuzzleConfiguration(wordConfiguration.getLetters(), crosswordLayout);
    }

    private PuzzleWordConfiguration generateWordConfiguration(
            PuzzleGenerationSettings generationSettings, ProcessedDictionary dictionary) {
        WordConfigurationGenerator wordConfigGenerator =
                new WordConfigurationGenerator(dictionary, new MaximisingWordConfigurationEvaluator())
                        .withMaximumWordCount(generationSettings.getMaxWords())
                        .withMinimumWordLength(generationSettings.getMinWordLength());
        return wordConfigGenerator.buildPuzzle(generationSettings.getNumLetters());
    }

    private CrosswordLayout generateLayout(
            PuzzleWordConfiguration wordConfiguration, ProcessedDictionary dictionary) {
        BoardGenerationFlags flags = getGenerationFlags();
        BoardEvaluator evaluator = new SimpleBoardEvaluator(flags);
        BoardGenerator generator = new BoardGenerator(evaluator, flags);
        Board generatedBoard = generator.generateBoard(wordConfiguration.getWords());

        return new CrosswordLayout(generatedBoard, dictionary);
    }

    private BoardGenerationFlags getGenerationFlags() {
        BoardGenerationFlags flags = new BoardGenerationFlags();
        flags.setFlag(BoardGenerationFlagConstant.RANDOM_INITIAL_ORIENTATION, true);
        flags.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);
        flags.setFlag(BoardGenerationFlagConstant.GENERATE_SEVERAL_BOARDS, true);
        flags.setFlag(BoardGenerationFlagConstant.PREFER_MORE_INTERSECTIONS, true);
        return flags;
    }
}
