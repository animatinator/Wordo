package com.animatinator.wordo.crossword.dictionary.processed;

import com.animatinator.wordo.crossword.dictionary.fingerprint.WordFingerPrint;
import com.animatinator.wordo.crossword.dictionary.util.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class ProcessedDictionaryTest {
    private ProcessedDictionary dictionary = new ProcessedDictionary();

    @Test
    public void noWordsOfLength() {
        TestUtils.assertEmpty(dictionary.getWordsOfLength(0));
        TestUtils.assertEmpty(dictionary.getWordsOfLength(1));
        TestUtils.assertEmpty(dictionary.getWordsOfLength(50));
    }

    @Test
    public void addWordOfLength() {
        dictionary.addWord("hello");
        List<String> wordsOfLengthFive = dictionary.getWordsOfLength(5);
        TestUtils.assertHasLength(1, wordsOfLengthFive);
        TestUtils.assertContains(wordsOfLengthFive, "hello");
    }

    @Test
    public void addWordOfLengthInExistingRange() {
        dictionary.addWord("hello");
        dictionary.addWord("one");
        TestUtils.assertHasLength(1, dictionary.getWordsOfLength(3));
        TestUtils.assertHasLength(1, dictionary.getWordsOfLength(5));
    }

    @Test
    public void addTwoWordsOfSameLength() {
        dictionary.addWord("word");
        dictionary.addWord("four");
        List<String> wordsOfLengthFour = dictionary.getWordsOfLength(4);
        TestUtils.assertHasLength(2, wordsOfLengthFour);
        TestUtils.assertContains(wordsOfLengthFour, "word", "four");
    }

    /**
     * Not an important case - just verifying an edge case.
     */
    @Test
    public void wordOfLengthZero() {
        dictionary.addWord("");
        List<String> wordsOfLengthZero = dictionary.getWordsOfLength(0);
        TestUtils.assertHasLength(1, wordsOfLengthZero);
        TestUtils.assertContains(wordsOfLengthZero, "");
    }

    @Test
    public void wordsOfLength_eAcute() {
        dictionary.addWord("intentaré");
        List<String> wordsOfLengthNine = dictionary.getWordsOfLength(9);
        TestUtils.assertHasLength(1, wordsOfLengthNine);
        TestUtils.assertContains(wordsOfLengthNine, "intentaré");
    }

    @Test
    public void wordsOfLength_iAcute() {
        dictionary.addWord("comí");
        List<String> wordsOfLengthFour = dictionary.getWordsOfLength(4);
        TestUtils.assertHasLength(1, wordsOfLengthFour);
        TestUtils.assertContains(wordsOfLengthFour, "comí");
    }

    @Test
    public void wordsOfLength_enye() {
        dictionary.addWord("niño");
        List<String> wordsOfLengthFour = dictionary.getWordsOfLength(4);
        TestUtils.assertHasLength(1, wordsOfLengthFour);
        TestUtils.assertContains(wordsOfLengthFour, "niño");
    }

    @Test
    public void addWordToProcessedDictionary() {
        dictionary.addWord("test");
        TestUtils.assertContains(dictionary.getDictionary(), new DictionaryEntry("test", new WordFingerPrint("estt".split(""))));
    }

}
