package com.animatinator.wordo.crossword.dictionary.puzzle;

import com.animatinator.wordo.crossword.dictionary.evaluate.MaximisingWordConfigurationEvaluator;
import com.animatinator.wordo.crossword.dictionary.match.WordMatcher;
import com.animatinator.wordo.crossword.dictionary.processed.DictionaryEntry;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;
import com.animatinator.wordo.crossword.dictionary.util.TestUtils;
import com.animatinator.wordo.crossword.testutils.TestProgressListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class WordConfigurationGeneratorTest {
    private WordConfigurationGenerator generator;
    private ProcessedDictionary dictionary;
    private WordMatcher matcher;

    @Before
    public void setUp() {
        try {
            dictionary = TestUtils.loadTestDictionary();
        } catch (IOException e) {
            fail("Couldn't load test dictionary! Exception: "+e);
        }
        generator = new WordConfigurationGenerator(
                dictionary, new MaximisingWordConfigurationEvaluator());
        matcher = new WordMatcher();
    }

    @Test
    public void puzzleOfLength() {
        PuzzleWordConfiguration puzzle = generator.buildPuzzle(4);
        assertEquals(4, puzzle.getNumberOfLettersRequired());
    }

    @Test
    public void puzzleWithNoLength() {
        PuzzleWordConfiguration puzzle = generator.buildPuzzle(0);
        TestUtils.assertHasLength(0, puzzle.getWords());
    }

    @Test
    public void puzzlesGeneratedAreValid() {
        // Run fifty times to account for randomness to some extent.
        for (int i = 0; i < 50; i++) {
            PuzzleWordConfiguration puzzle = generator.buildPuzzle(6);
            TestUtils.assertHasLength(14, puzzle.getWords());
            assertWordsAllInDictionary(puzzle.getWords(), dictionary);
            assertWordsAllFormableFromBaseWord(puzzle.getWords(), getLongestWord(puzzle));
        }
    }

    @Test
    public void puzzleWithZeroWords() {
        PuzzleWordConfiguration puzzle = generator.withMaximumWordCount(0).buildPuzzle(6);
        TestUtils.assertHasLength(0, puzzle.getWords());
    }

    @Test
    public void puzzleWithOnlyOneWord() {
        PuzzleWordConfiguration puzzle = generator.withMaximumWordCount(1).buildPuzzle(6);
        TestUtils.assertHasLength(1, puzzle.getWords());
    }

    @Test
    public void puzzleWithMinimumWordLength() {
        PuzzleWordConfiguration puzzle = generator.withMinimumWordLength(6).buildPuzzle(6);
        TestUtils.assertHasLength(1, puzzle.getWords());
    }

    @Test
    public void puzzleWithMinimumLengthTooHigh() {
        PuzzleWordConfiguration puzzle = generator.withMinimumWordLength(7).buildPuzzle(6);
        TestUtils.assertHasLength(0, puzzle.getWords());
    }

    @Test
    public void puzzleWithInitialLengthTooHigh() {
        PuzzleWordConfiguration puzzle = generator.withMinimumWordLength(3).buildPuzzle(10);
        assertEquals(6, puzzle.getNumberOfLettersRequired());
    }

    @Test
    public void setOfLettersIsCorrect() {
        PuzzleWordConfiguration puzzle = generator.withMinimumWordLength(3).buildPuzzle(6);
        TestUtils.assertContains(Arrays.asList(puzzle.getLetters()), "c", "a", "u", "s", "e", "d");
    }

    @Test
    public void progressMeterIncreasesAsExpected() {
        TestProgressListener progressListener = new TestProgressListener();
        PuzzleWordConfiguration puzzle =
                generator.withMinimumWordLength(3).buildPuzzle(6, progressListener);
        assertEquals(1.0d, progressListener.getProgress(), 0.01d);
    }

    private void assertWordsAllInDictionary(List<String> words, ProcessedDictionary dictionary) {
        List<String> dictionaryWords = dictionary.getGenerationDictionary().stream().map(DictionaryEntry::word).collect(Collectors.toList());
        TestUtils.assertContains(dictionaryWords, words);
    }

    private void assertWordsAllFormableFromBaseWord(List<String> words, String baseWord) {
        for (String word : words) {
            assertTrue(matcher.firstWordCanFormSecond(baseWord, word));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private String getLongestWord(PuzzleWordConfiguration puzzle) {
        return puzzle.getWords().stream().max(Comparator.comparingInt(String::length)).get();
    }

}
