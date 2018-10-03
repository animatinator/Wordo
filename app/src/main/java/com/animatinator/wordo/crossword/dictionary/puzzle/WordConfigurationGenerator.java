package com.animatinator.wordo.crossword.dictionary.puzzle;

import android.util.Log;

import com.animatinator.wordo.crossword.dictionary.evaluate.WordConfigurationEvaluator;
import com.animatinator.wordo.crossword.dictionary.fingerprint.FingerPrinter;
import com.animatinator.wordo.crossword.dictionary.fingerprint.WordFingerPrint;
import com.animatinator.wordo.crossword.dictionary.match.WordMatcher;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;

import java.util.*;
import java.util.stream.Collectors;

public class WordConfigurationGenerator {

    private static final String TAG = "WordConfigurationGenerator";

    private static final int WORDS_TO_TRY = 50;

    private final int minimumWordLength;
    private final int maximumWordCount;

    private final ProcessedDictionary dictionary;
    private final WordMatcher matcher;
    private final WordConfigurationEvaluator configEvaluator;

    public WordConfigurationGenerator(
            ProcessedDictionary dictionary, WordConfigurationEvaluator configEvaluator) {
        this(dictionary, 0, 1000, configEvaluator);
    }

    private WordConfigurationGenerator(
            ProcessedDictionary dictionary,
            int minimumWordLength,
            int maximumWordCount,
            WordConfigurationEvaluator configEvaluator) {
        this.dictionary = dictionary;
        this.minimumWordLength = minimumWordLength;
        this.maximumWordCount = maximumWordCount;
        this.configEvaluator = configEvaluator;
        matcher = new WordMatcher();
    }

    public WordConfigurationGenerator withMinimumWordLength(int newMinimumWordLength) {
        return new WordConfigurationGenerator(
                dictionary, newMinimumWordLength, maximumWordCount, configEvaluator);
    }

    public WordConfigurationGenerator withMaximumWordCount(int newMaximumWordCount) {
        return new WordConfigurationGenerator(
                dictionary, minimumWordLength, newMaximumWordCount, configEvaluator);
    }

    public PuzzleWordConfiguration buildPuzzle(int numLetters) {
        if (numLetters == 0) {
            return PuzzleWordConfiguration.EMPTY_PUZZLE;
        }

        List<String> possibleBaseWords = chooseBaseWords(numLetters);
        if (possibleBaseWords.isEmpty()) {
            Log.e(TAG, "Couldn't find an appropriate base word!");
            return PuzzleWordConfiguration.EMPTY_PUZZLE;
        }

        String bestBaseWord = null;
        float bestBaseWordScore = 0.0f;

        for (String word : possibleBaseWords) {
            PuzzleWordConfiguration config = generateConfigForBaseWord(word, false);
            float score = configEvaluator.evaluateWordConfig(config);

            if (score > bestBaseWordScore) {
                bestBaseWord = word;
                bestBaseWordScore = score;
            }
        }

        // If for some reason we couldn't get a non-zero score, return an empty puzzle.
        if (bestBaseWord == null) {
            Log.e(TAG, "Couldn't pick a base word with a non-zero score!");
            return PuzzleWordConfiguration.EMPTY_PUZZLE;
        }

        // Generate a layout for the best base word, sticking to the word limit this time.
        return generateConfigForBaseWord(bestBaseWord, true);
    }

    private PuzzleWordConfiguration generateConfigForBaseWord(
            String baseWord, boolean stickToLimit) {
        WordFingerPrint baseWordFingerPrint = FingerPrinter.getFingerprint(baseWord);
        List<String> allWords = matcher.getWordsFormableFromWord(baseWord, dictionary);

        allWords = allWords.stream().filter(word -> word.length() >= minimumWordLength).collect(Collectors.toList());

        final List<String> words;

        if (stickToLimit && maximumWordCount < allWords.size()) {
            words = randomlySelectNFromList(allWords, maximumWordCount);
        } else {
            words = allWords;
        }

        return new PuzzleWordConfiguration(
                baseWordFingerPrint.getCharacters(), words, baseWord.length());
    }

    private List<String> chooseBaseWords(int numLetters) {
        List<String> result = new ArrayList<>();

        while (result.isEmpty() && numLetters > 0) {
            result = chooseBaseWordsForFixedLength(numLetters);
            numLetters--;
        }

        return result;
    }

    private List<String> chooseBaseWordsForFixedLength(int numLetters) {
        List<String> possibleBaseWords = dictionary.getWordsOfLength(numLetters);

        if (possibleBaseWords.size() == 0) {
            return new ArrayList<>();
        }

        return randomlySelectNFromList(possibleBaseWords, WORDS_TO_TRY);
    }

    private List<String> randomlySelectNFromList(List<String> input, int limit) {
        List<String> copy = new LinkedList<>(input);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(input.size(), limit));
    }
}
