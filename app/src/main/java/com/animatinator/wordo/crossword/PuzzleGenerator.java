package com.animatinator.wordo.crossword;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.dictionary.evaluate.PreferLongerWordsAndRightNumberWordConfigurationEvaluator;
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

    // The fractions each stage makes up of the overall work. Very rough, used to scale progress
    // reporting from each one.
    private static final double WORD_CONFIG_PROGRESS_FRACTION = 0.6d;
    private static final double LAYOUT_GENERATION_PROGRESS_FRACTION = 0.4d;

    private final ProcessedDictionary dictionary;

    public PuzzleGenerator(ProcessedDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public PuzzleConfiguration createPuzzle(
            PuzzleGenerationSettings generationSettings,
            PuzzleGenerationProgressCallback progressCallback) {
        progressCallback.setProgress(0.0d);
        progressCallback.setGenerationState("Generating set of letters and words");


        PuzzleWordConfiguration wordConfiguration =
                generateWordConfiguration(
                        generationSettings,
                        dictionary,
                        progress ->
                                progressCallback.setProgress(
                                        progress * WORD_CONFIG_PROGRESS_FRACTION));

        progressCallback.setGenerationState("Generating board layout");
        CrosswordLayout crosswordLayout =
                generateLayout(
                        wordConfiguration,
                        dictionary,
                        progress ->
                                progressCallback.setProgress(
                                        WORD_CONFIG_PROGRESS_FRACTION +
                                                (progress * LAYOUT_GENERATION_PROGRESS_FRACTION)));

        return new PuzzleConfiguration(wordConfiguration.getLetters(), crosswordLayout);
    }

    private PuzzleWordConfiguration generateWordConfiguration(
            PuzzleGenerationSettings generationSettings,
            ProcessedDictionary dictionary,
            ProgressCallback progressCallback) {
        WordConfigurationGenerator wordConfigGenerator =
                new WordConfigurationGenerator(
                        dictionary,
                        new PreferLongerWordsAndRightNumberWordConfigurationEvaluator(
                                generationSettings.getMaxWords()))
                        .withMaximumWordCount(generationSettings.getMaxWords())
                        .withMinimumWordLength(generationSettings.getMinWordLength());
        return wordConfigGenerator.buildPuzzle(
                generationSettings.getNumLetters(), progressCallback);
    }

    private CrosswordLayout generateLayout(
            PuzzleWordConfiguration wordConfiguration,
            ProcessedDictionary dictionary,
            ProgressCallback progressCallback) {
        BoardGenerationFlags flags = getGenerationFlags();
        BoardEvaluator evaluator = new SimpleBoardEvaluator(flags);
        BoardGenerator generator = new BoardGenerator(evaluator, flags);
        Board generatedBoard =
                generator.generateBoard(wordConfiguration.getWords(), progressCallback);

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
