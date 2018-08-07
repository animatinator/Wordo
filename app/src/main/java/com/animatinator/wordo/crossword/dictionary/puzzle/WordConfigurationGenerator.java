package com.animatinator.wordo.crossword.dictionary.puzzle;

import com.animatinator.wordo.crossword.dictionary.fingerprint.FingerPrinter;
import com.animatinator.wordo.crossword.dictionary.fingerprint.WordFingerPrint;
import com.animatinator.wordo.crossword.dictionary.match.WordMatcher;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;

import java.util.*;
import java.util.stream.Collectors;

public class WordConfigurationGenerator {

    private static final PuzzleWordConfiguration EMPTY_PUZZLE =
            new PuzzleWordConfiguration(new String[]{}, new ArrayList<>(), new ArrayList<>(), 0);

    private final int minimumWordLength;
    private final int maximumWordCount;

    private final ProcessedDictionary dictionary;
    private final Random random;
    private final WordMatcher matcher;

    public WordConfigurationGenerator(ProcessedDictionary dictionary) {
        this(dictionary, 0, 1000);
    }

    private WordConfigurationGenerator(ProcessedDictionary dictionary, int minimumWordLength, int maximumWordCount) {
        this.dictionary = dictionary;
        this.minimumWordLength = minimumWordLength;
        this.maximumWordCount = maximumWordCount;
        random  = new Random();
        matcher = new WordMatcher();
    }

    public WordConfigurationGenerator withMinimumWordLength(int newMinimumWordLength) {
        return new WordConfigurationGenerator(dictionary, newMinimumWordLength, maximumWordCount);
    }

    public WordConfigurationGenerator withMaximumWordCount(int newMaximumWordCount) {
        return new WordConfigurationGenerator(dictionary, minimumWordLength, newMaximumWordCount);
    }

    public PuzzleWordConfiguration buildPuzzle(int numLetters) {
        if (numLetters == 0) {
            return EMPTY_PUZZLE;
        }

        Optional<String> possibleBaseWord = chooseBaseWord(numLetters);
        if (!possibleBaseWord.isPresent()) {
            return EMPTY_PUZZLE;
        }

        // TODO: Consider trying a few base words and picking the one with the 'best' options by some criterion.
        String baseWord = possibleBaseWord.get();
        WordFingerPrint baseWordFingerPrint = FingerPrinter.getFingerprint(baseWord);
        numLetters = baseWord.length();
        List<String> allWords = matcher.getWordsFormableFromWord(baseWord, dictionary);

        allWords = allWords.stream().filter(word -> word.length() >= minimumWordLength).collect(Collectors.toList());

        final List<String> words;

        if (maximumWordCount < allWords.size()) {
            words = randomlySelectNFromList(allWords, maximumWordCount);
        } else {
            words = allWords;
        }

        List<String> bonusWords =
                allWords.stream().filter(word -> !words.contains(word)).collect(Collectors.toList());

        return new PuzzleWordConfiguration(
                baseWordFingerPrint.getCharacters(), words, bonusWords, numLetters);
    }

    private Optional<String> chooseBaseWord(int numLetters) {
        Optional<String> result = Optional.empty();

        while (!result.isPresent() && numLetters > 0) {
            result = chooseBaseWordForFixedLength(numLetters);
            numLetters--;
        }

        return result;
    }

    private Optional<String> chooseBaseWordForFixedLength(int numLetters) {
        List<String> possibleBaseWords = dictionary.getWordsOfLength(numLetters);

        if (possibleBaseWords.size() == 0) {
            return Optional.empty();
        }

        int indexToUse = random.nextInt(possibleBaseWords.size());
        return Optional.of(possibleBaseWords.get(indexToUse));
    }

    private List<String> randomlySelectNFromList(List<String> input, int limit) {
        List<String> copy = new LinkedList<>(input);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(input.size(), limit));
    }
}
