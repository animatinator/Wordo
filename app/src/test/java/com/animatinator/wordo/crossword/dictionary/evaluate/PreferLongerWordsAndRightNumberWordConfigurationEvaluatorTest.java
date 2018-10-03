package com.animatinator.wordo.crossword.dictionary.evaluate;

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
public class PreferLongerWordsAndRightNumberWordConfigurationEvaluatorTest {
    private final String[] letters = {"t", "e", "s", "t", "i", "n", "g"};
    private final PreferLongerWordsAndRightNumberWordConfigurationEvaluator evaluator =
            new PreferLongerWordsAndRightNumberWordConfigurationEvaluator(5);

    private PuzzleWordConfiguration configWithThreeWords;
    private PuzzleWordConfiguration configWithFiveWords;
    private PuzzleWordConfiguration configWithSixWordsAndLongerAverage;
    private PuzzleWordConfiguration configWithSevenWords;

    @Before
    public void setUpConfigs() {
        List<String> threeWords = new ArrayList<>();
        threeWords.add("test");
        threeWords.add("est");
        threeWords.add("tet");
        configWithThreeWords = new PuzzleWordConfiguration(letters, threeWords, 7);

        List<String> fiveWords = new ArrayList<>(threeWords);
        fiveWords.add("set");
        fiveWords.add("tes");
        configWithFiveWords = new PuzzleWordConfiguration(letters, fiveWords, 7);

        List<String> sixWordsAndLongerAverage = new ArrayList<>(fiveWords);
        sixWordsAndLongerAverage.add("testingtestingtestingtesting");
        configWithSixWordsAndLongerAverage = new PuzzleWordConfiguration(letters, sixWordsAndLongerAverage, 7);

        List<String> sevenWords = new ArrayList<>(fiveWords);
        sevenWords.add("ting");
        sevenWords.add("int");
        configWithSevenWords = new PuzzleWordConfiguration(letters, sevenWords, 7);
    }

    @Test
    public void emptyConfig() {
        assertEquals(
                0.0f, evaluator.evaluateWordConfig(PuzzleWordConfiguration.EMPTY_PUZZLE), 0.01f);
    }

    @Test
    public void evaluatePerfectMatch() {
        float perfectMatchScore = evaluator.evaluateWordConfig(configWithFiveWords);
        assertEquals(3.2f, perfectMatchScore, 0.01d);
    }

    @Test
    public void tooManyTooFewEquallyBad() {
        float threeWordsScore = evaluator.evaluateWordConfig(configWithThreeWords);
        float sevenWordsScore = evaluator.evaluateWordConfig(configWithSevenWords);
        assertEquals(threeWordsScore, sevenWordsScore, 0.01d);
    }

    @Test
    public void shortWordsGoodLongWordsBetter() {
        float fiveWordsScore = evaluator.evaluateWordConfig(configWithFiveWords);
        float sixWordsScore = evaluator.evaluateWordConfig(configWithSixWordsAndLongerAverage);
        assertTrue(sixWordsScore > fiveWordsScore);
    }
}