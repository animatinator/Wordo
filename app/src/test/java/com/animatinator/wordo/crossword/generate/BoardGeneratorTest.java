package com.animatinator.wordo.crossword.generate;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.evaluate.BoardEvaluator;
import com.animatinator.wordo.crossword.evaluate.SimpleBoardEvaluator;
import com.animatinator.wordo.crossword.print.BoardPrinter;
import com.animatinator.wordo.crossword.print.BoardToHumanReadableString;
import com.animatinator.wordo.crossword.print.SystemOutPrinter;
import com.animatinator.wordo.crossword.testutils.TestProgressListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class BoardGeneratorTest {
    private static final String[] WORDS = new String[]{
            "ads", "cue", "sad", "sea", "sue", "use", "aces", "case", "cues", "dues", "used", "cause", "sauce",
            "caused"};
    private static final int ITERATIONS = 100;

    @Test
    public void evaluateDefaultBoardGeneration() {
        BoardGenerationFlags flags = new BoardGenerationFlags();
        BoardEvaluator evaluator = new SimpleBoardEvaluator(flags);
        BoardGenerator generator = new BoardGenerator(evaluator, flags);
        double[] results = new double[ITERATIONS];
        double best = -1.0d;
        Board bestBoard = null;

        for (int i = 0; i < ITERATIONS; i++) {
            Board board = generator.generateBoard(Arrays.asList(WORDS));
            double fitness = evaluator.evaluateBoard(board);
            results[i] = fitness;
            if (fitness > best) {
                bestBoard = board;
                best = fitness;
            }
        }

        System.out.println("Evaluating the default board generation method:");
        System.out.println("Average board: "+getMean(results));
        System.out.println("Best board: "+best);
        new BoardPrinter(new SystemOutPrinter(), new BoardToHumanReadableString()).printBoard(bestBoard);
        System.out.println();
    }

    /**
     * This comparison is meaningless because the random initial orientation doesn't affect the score, and there's
     * nothing else random enabled.
     */
    @Test
    public void compareSingleBoardWithSeveral() {
        BoardGenerationFlags flagsWithMultiGenerationEnabled = new BoardGenerationFlags();
        flagsWithMultiGenerationEnabled.setFlag(BoardGenerationFlagConstant.GENERATE_SEVERAL_BOARDS, true);
        compareFlagSets(new BoardGenerationFlags(), flagsWithMultiGenerationEnabled);
    }

    /**
     * Picking randomly from the top few word placements at each stage seems to be better than just picking the best
     * every time.
     */
    @Test
    public void compareRandomWordSelectionWithBasic() {
        BoardGenerationFlags flagsWithRandomGeneration = new BoardGenerationFlags();
        flagsWithRandomGeneration.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);
        compareFlagSets(new BoardGenerationFlags(), flagsWithRandomGeneration);
    }

    /**
     * Generating several boards and picking the best seems to improve the mean generated board where we're selecting
     * words randomly.
     */
    @Test
    public void compareSingleBoardWithSeveral_randomWordSelection() {
        BoardGenerationFlags flagsWithMultiGenerationEnabled = new BoardGenerationFlags();
        flagsWithMultiGenerationEnabled.setFlag(BoardGenerationFlagConstant.GENERATE_SEVERAL_BOARDS, true);
        flagsWithMultiGenerationEnabled.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);

        BoardGenerationFlags justRandomWordSelection = new BoardGenerationFlags();
        justRandomWordSelection.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);

        compareFlagSets(flagsWithMultiGenerationEnabled, justRandomWordSelection);
    }

    /**
     * BoardGenerationFlagConstant#PREFER_MORE_INTERSECTIONS} is consistently /a bit/ better than the existing
     * algorithm, but the difference is surprisingly small.
     *
     * This test use a board evaluator which prefers more intersections in evaluating both flag sets to give comparable
     * results.
     */
    @Test
    public void comparePreferMoreIntersections() {
        BoardGenerationFlags flagsWithMoreIntersectionsPreferred = new BoardGenerationFlags();
        flagsWithMoreIntersectionsPreferred.setFlag(BoardGenerationFlagConstant.GENERATE_SEVERAL_BOARDS, true);
        flagsWithMoreIntersectionsPreferred.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);
        flagsWithMoreIntersectionsPreferred.setFlag(BoardGenerationFlagConstant.PREFER_MORE_INTERSECTIONS, true);

        BoardGenerationFlags flagsWithoutMoreIntersectionsPreferred = new BoardGenerationFlags();
        flagsWithoutMoreIntersectionsPreferred.setFlag(BoardGenerationFlagConstant.GENERATE_SEVERAL_BOARDS, true);
        flagsWithoutMoreIntersectionsPreferred.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);

        compareFlagSets(
                new SimpleBoardEvaluator(flagsWithMoreIntersectionsPreferred),
                flagsWithMoreIntersectionsPreferred,
                flagsWithoutMoreIntersectionsPreferred);
    }

    @Test
    public void testProgress_singleBoard() {
        BoardGenerationFlags flags = new BoardGenerationFlags();
        flags.setFlag(BoardGenerationFlagConstant.RANDOM_INITIAL_ORIENTATION, true);
        flags.setFlag(BoardGenerationFlagConstant.GENERATE_SEVERAL_BOARDS, false);
        flags.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);
        flags.setFlag(BoardGenerationFlagConstant.PREFER_MORE_INTERSECTIONS, true);

        BoardGenerator generator = new BoardGenerator(new SimpleBoardEvaluator(flags), flags);
        TestProgressListener progressListener = new TestProgressListener();

        generator.generateBoard(Arrays.asList(WORDS), progressListener);

        assertEquals(1.0d, progressListener.getProgress(), 0.01d);
    }

    @Test
    public void testProgress_multipleBoards() {
        BoardGenerationFlags flags = new BoardGenerationFlags();
        flags.setFlag(BoardGenerationFlagConstant.RANDOM_INITIAL_ORIENTATION, true);
        flags.setFlag(BoardGenerationFlagConstant.GENERATE_SEVERAL_BOARDS, true);
        flags.setFlag(BoardGenerationFlagConstant.PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS, true);
        flags.setFlag(BoardGenerationFlagConstant.PREFER_MORE_INTERSECTIONS, true);

        BoardGenerator generator = new BoardGenerator(new SimpleBoardEvaluator(flags), flags);
        TestProgressListener progressListener = new TestProgressListener();

        generator.generateBoard(Arrays.asList(WORDS), progressListener);

        assertEquals(1.0d, progressListener.getProgress(), 0.01d);
    }

    private void compareFlagSets(BoardGenerationFlags ... flags) {
        compareFlagSets(null, flags);
    }

    private void compareFlagSets(BoardEvaluator overridingResultEvaluator, BoardGenerationFlags ... flags) {
        System.out.println("Flag set comparison:");
        List<FitnessResult> results = evaluateGenerationWithFlags(overridingResultEvaluator, flags);
        results.sort(Comparator.comparingDouble(FitnessResult::getMean).reversed());
        for (FitnessResult result : results) {
            System.out.println("Flags: "+result.getFlags()+"\n\tMean fitness: "+result.getMean()+"\n\tMax fitness: "+ result.getMax());
        }
        System.out.println();
    }

    private List<FitnessResult> evaluateGenerationWithFlags(
            BoardEvaluator overridingResultEvaluator, BoardGenerationFlags ... flags) {
        List<FitnessResult> results = new ArrayList<>();

        for (BoardGenerationFlags flagSet : flags) {
            results.add(evaluateBoardGeneration(overridingResultEvaluator, flagSet));
        }

        return results;
    }

    private FitnessResult evaluateBoardGeneration(
            BoardEvaluator overridingResultEvaluator, BoardGenerationFlags flags) {
        BoardEvaluator evaluator = new SimpleBoardEvaluator(flags);
        BoardEvaluator resultsEvaluator =
                (overridingResultEvaluator != null) ? overridingResultEvaluator : evaluator;
        BoardGenerator generator = new BoardGenerator(evaluator, flags);
        double sum = 0.0d;
        double best = -1.0d;

        for (int i = 0; i < ITERATIONS; i++) {
            Board board = generator.generateBoard(Arrays.asList(WORDS));
            double fitness = resultsEvaluator.evaluateBoard(board);
            sum += fitness;
            best = Math.max(best, fitness);
        }

        return new FitnessResult(flags, sum / ITERATIONS, best);
    }

    private static double getMean(double[] values) {
        double sum = 0.0d;

        for (double value : values) {
            sum += value;
        }

        return sum / (double)values.length;
    }

    private static class FitnessResult {
        private final BoardGenerationFlags flags;
        private final double mean;
        private final double max;

        FitnessResult(BoardGenerationFlags flags, double mean, double max) {
            this.flags = flags;
            this.mean = mean;
            this.max = max;
        }

        double getMean() {
            return mean;
        }

        double getMax() {
            return max;
        }

        BoardGenerationFlags getFlags() {
            return flags;
        }
    }
}
