package com.animatinator.wordo.crossword.dictionary.util;

import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestUtils {

    private static final String TEST_DICTIONARY_PATH =
            "src\\test\\java\\com\\animatinator\\wordo\\crossword\\dictionary\\data\\basicdict.txt";

    public static <T> void assertEmpty(List<T> list) {
        assertTrue(list.isEmpty());
    }

    public static <T> void assertHasLength(int expected, List<T> list) {
        assertEquals(expected, list.size());
    }

    @SafeVarargs
    public static <T> void assertContains(List<T> list, T... items) {
        assertTrue(list.containsAll(Arrays.asList(items)));
    }

    public static <T> void assertContains(List<T> list, List<T> items) {
        assertTrue(list.containsAll(items));
    }

    public static ProcessedDictionary loadTestDictionary() throws IOException {
        ProcessedDictionary dictionary = new ProcessedDictionary();

        String rawDictionary = new String(Files.readAllBytes(Paths.get(TEST_DICTIONARY_PATH)));
        for (String word : rawDictionary.split(System.getProperty("line.separator"))) {
            dictionary.addWordToGenerationDictionary(word);
        }

        return dictionary;
    }
}
