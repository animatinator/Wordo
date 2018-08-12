package com.animatinator.wordo.crossword.dictionary.evaluate;

import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class OptimisingWordConfigurationEvaluatorTest {
    private final String[] letters = {"t", "e", "s", "t", "i", "n", "g"};
    private final OptimisingWordConfigurationEvaluator evaluator =
            new OptimisingWordConfigurationEvaluator(5);

    // Two words too few.
    private PuzzleWordConfiguration configWithThreeWords;
    // One too many, technically optimal per the target word ratio specified in the class.
    private PuzzleWordConfiguration configWithSixWords;
    // Four words too many.
    private PuzzleWordConfiguration configWithNineWords;

    @Before
    public void setUpConfigs() {
        List<String> threeWords = new ArrayList<>();
        threeWords.add("test");
        threeWords.add("est");
        threeWords.add("tet");
        configWithThreeWords = new PuzzleWordConfiguration(letters, threeWords, 7);

        List<String> sixWords = new ArrayList<>(threeWords);
        sixWords.add("set");
        sixWords.add("tes");
        sixWords.add("ting");
        configWithSixWords = new PuzzleWordConfiguration(letters, sixWords, 7);

        List<String> nineWords = new ArrayList<>(sixWords);
        nineWords.add("int");
        nineWords.add("sting");
        nineWords.add("sing");
        configWithNineWords = new PuzzleWordConfiguration(letters, nineWords, 7);
    }

    @Test
    public void emptyConfig() {
        assertEquals(
                0.0f, evaluator.evaluateWordConfig(PuzzleWordConfiguration.EMPTY_PUZZLE), 0.01f);
    }

    /**
     * Having slightly more than needed is optimal. See the target word ratio in {@link
     * OptimisingWordConfigurationEvaluator}.
     */
    @Test
    public void evaluatePerfectMatch() {
        assertEquals(1.0f, evaluator.evaluateWordConfig(configWithSixWords), 0.01f);
    }

    /**
     * Having four too many in this case is better than two too few.
     */
    @Test
    public void tooManyBetterThanTooFew() {
        float threeWordsScore = evaluator.evaluateWordConfig(configWithThreeWords);
        float nineWordsScore = evaluator.evaluateWordConfig(configWithNineWords);
        assertTrue(nineWordsScore > threeWordsScore);
    }
}