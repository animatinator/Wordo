package com.animatinator.wordo.crossword.dictionary.evaluate;

import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class MaximisingWordConfigurationEvaluatorTest {

    private final MaximisingWordConfigurationEvaluator maximisingEvaluator =
            new MaximisingWordConfigurationEvaluator();

    @Test
    public void evaluateEmptyConfig() {
        assertEquals(
                0.0f,
                maximisingEvaluator.evaluateWordConfig(PuzzleWordConfiguration.EMPTY_PUZZLE),
                0.1f);
    }

    @Test
    public void moreWordsMeansHigherScore() {
        PuzzleWordConfiguration fewWords =
                new PuzzleWordConfiguration(
                        new String[]{"t", "e", "s", "t"},
                        new ArrayList<String>() {{add("test");}},
                        4);
        PuzzleWordConfiguration moreWords =
                new PuzzleWordConfiguration(
                        new String[]{"t", "e", "s", "t"},
                        new ArrayList<String>() {{add("test"); add("set"); add("et");}},
                        4);
        float fewWordsScore = maximisingEvaluator.evaluateWordConfig(fewWords);
        float moreWordsScore = maximisingEvaluator.evaluateWordConfig(moreWords);
        assertTrue(moreWordsScore > fewWordsScore);
    }
}