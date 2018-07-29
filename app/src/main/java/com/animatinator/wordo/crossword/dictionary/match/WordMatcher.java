package com.animatinator.wordo.crossword.dictionary.match;

import com.animatinator.wordo.crossword.dictionary.fingerprint.FingerPrinter;
import com.animatinator.wordo.crossword.dictionary.fingerprint.WordFingerPrint;
import com.animatinator.wordo.crossword.dictionary.processed.DictionaryEntry;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WordMatcher {

    /**
     * Takes a word to match in the constructor and matches {@link DictionaryEntry} objects which are formable from the
     * word to match.
     */
    private static final class DictionaryWordMatcher implements Predicate<DictionaryEntry> {
        private final WordFingerPrint wordToMatch;

        private DictionaryWordMatcher(String wordToMatch) {
            this.wordToMatch = FingerPrinter.getFingerprint(wordToMatch);
        }

        @Override
        public boolean test(DictionaryEntry dictionaryEntry) {
            return firstWordCanFormSecond(wordToMatch, dictionaryEntry.fingerPrint());
        }
    }

    static boolean firstWordCanFormSecond(WordFingerPrint first, WordFingerPrint second) {
        String[] firstCharacters = first.getCharacters();
        String[] secondCharacters = second.getCharacters();
        int firstIndex = 0, secondIndex = 0;

        while (firstIndex < firstCharacters.length && secondIndex < secondCharacters.length) {
            String firstChar = firstCharacters[firstIndex];
            String secondChar = secondCharacters[secondIndex];

            if (firstChar.equalsIgnoreCase(secondChar)) {
                firstIndex++;
                secondIndex++;
            } else if (firstChar.compareToIgnoreCase(secondChar) < 0) {
                firstIndex++;
            } else {
                return false;
            }
        }

        // Fail if there are still characters left in the word to be formed.
        //noinspection RedundantIfStatement
        if (secondIndex < secondCharacters.length) {
            return false;
        }

        return true;
    }

    public boolean firstWordCanFormSecond(String first, String second) {
        return firstWordCanFormSecond(FingerPrinter.getFingerprint(first), FingerPrinter.getFingerprint(second));
    }

    public List<String> getWordsFormableFromWord(String word, ProcessedDictionary dictionary) {
        Predicate<DictionaryEntry> dictionaryEntryPredicate = new DictionaryWordMatcher(word);
        return dictionary.getDictionary().parallelStream()
                .filter(dictionaryEntryPredicate)
                .map(DictionaryEntry::word)
                .collect(Collectors.toList());
    }
}
