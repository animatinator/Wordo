package com.animatinator.wordo.crossword.dictionary.processed;

import com.animatinator.wordo.crossword.dictionary.fingerprint.FingerPrinter;
import com.animatinator.wordo.crossword.dictionary.fingerprint.WordFingerPrint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a dictionary once it's been processed. This consists of a few pieces:
 * * Map from int length to words of that length
 * * List of {@link DictionaryEntry}, the 'generation dictionary' used in building puzzles
 * * A big ol' hash set of all possible words in the 'language' used for bonus words (but not all
 * for generation).
 */
public class ProcessedDictionary {
    private ArrayList<List<String>> wordsOfLength = new ArrayList<>();
    private ArrayList<DictionaryEntry> processedDictionary = new ArrayList<>();
    private HashSet<String> wordsAdded = new HashSet<>();
    private HashSet<String> fullDictionary = new HashSet<>();

    public void addWordToGenerationDictionary(String word) {
        if (wordsAdded.contains(word)) {
            throw new IllegalArgumentException("Word '"+word+"' already in dictionary!");
        }

        addWordToWordsOfLength(word);
        addWordToProcessedDictionary(word);
        wordsAdded.add(word);
    }

    private void addWordToWordsOfLength(String word) {
        int length = word.length();

        if (length < wordsOfLength.size() && wordsOfLength.get(length) != null) {
            wordsOfLength.get(length).add(word);
        } else {
            expandToLength(length);
            List<String> newEntry = new ArrayList<>();
            newEntry.add(word);
            wordsOfLength.add(length, newEntry);
        }
    }

    private void addWordToProcessedDictionary(String word) {
        WordFingerPrint fingerPrint = FingerPrinter.getFingerprint(word);
        processedDictionary.add(new DictionaryEntry(word, fingerPrint));
    }

    public void addWordToFullDictionary(String word) {
        if (fullDictionary.contains(word)) {
            throw new IllegalArgumentException("Word '"+word+"' already in dictionary!");
        }
        fullDictionary.add(word);
    }

    public boolean isWordInFullDictionary(String word) {
        return fullDictionary.contains(word);
    }

    public List<DictionaryEntry> getGenerationDictionary() {
        return processedDictionary;
    }

    public List<String> getWordsOfLength(int length) {
        if (length < wordsOfLength.size()) {
            return wordsOfLength.get(length);
        }

        return new ArrayList<>();
    }

    private void expandToLength(int length) {
        for (int i = wordsOfLength.size(); i < length; i++) {
            wordsOfLength.add(new ArrayList<>());
        }
        wordsOfLength.ensureCapacity(length);
    }
}
