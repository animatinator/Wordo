package com.animatinator.wordo.dictionaries;

import com.animatinator.wordo.crossword.dictionary.fingerprint.WordFingerPrint;
import com.animatinator.wordo.crossword.dictionary.processed.DictionaryEntry;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "..\\app\\src\\main\\AndroidManifest.xml")
public class DictionaryLoaderTest {
    private static final String TEST_DICTIONARY_PATH = "basicdict.txt";

    @Test
    public void loadTestDictionary() throws IOException {
        DictionaryLoader loader = new DictionaryLoader(RuntimeEnvironment.application);
        ProcessedDictionary dictionary = loader.loadDictionary(TEST_DICTIONARY_PATH);
        List<DictionaryEntry> words = dictionary.getDictionary();

        // We got everything excluding comments.
        assertEquals(14, dictionary.getDictionary().size());
        // Check some words.
        assertTrue(
                words.contains(
                        new DictionaryEntry(
                                "aces",
                                new WordFingerPrint(new String[] {"a", "c", "e", "s"}))));
        assertTrue(
                words.contains(
                        new DictionaryEntry(
                                "use",
                                new WordFingerPrint(new String[] {"e", "s", "u"}))));
    }

    @Test
    public void loadNonExistentDictionary() {
        DictionaryLoader loader = new DictionaryLoader(RuntimeEnvironment.application);
        try {
            loader.loadDictionary("nope");
        } catch (IOException expected) {
            return;
        }

        fail("Shouldn't be able to load non-existent dictionary!");
    }
}