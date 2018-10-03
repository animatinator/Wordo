package com.animatinator.wordo.dictionaries;

import android.content.Context;
import android.content.res.AssetManager;

import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DictionaryLoader {
    private static final File DICTIONARY_ASSETS_ROOT = new File("dictionaries");
    // It's possible to use a smaller version of the dictionary for generation, but right now we
    // don't. This is left here for debugging purposes (it only adds a couple of lines of
    // complexity).
    private static final String FULL_DICTIONARY_NAME = "full.txt";
    private static final String GENERATION_DICTIONARY_NAME = "full.txt";

    private AssetManager assetManager;

    public DictionaryLoader(Context context) {
        assetManager = context.getAssets();
    }

    public ProcessedDictionary loadDictionary(String name) throws IOException {
        File dictionaryFolder = new File(DICTIONARY_ASSETS_ROOT, name);
        File smallDictionary = new File(dictionaryFolder, GENERATION_DICTIONARY_NAME);
        File fullDictionary = new File(dictionaryFolder, FULL_DICTIONARY_NAME);

        ProcessedDictionary result = new ProcessedDictionary();

        loadWords(smallDictionary, result::addWordToGenerationDictionary);
        loadWords(fullDictionary, result::addWordToFullDictionary);

        return result;
    }

    private void loadWords(File wordsSource, WordProcessor wordProcessor) throws IOException {
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(assetManager.open(wordsSource.getPath())));

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            if (currentLine.startsWith("#")) {
                continue;
            }
            wordProcessor.processWord(currentLine.toUpperCase());
        }
    }

    private interface WordProcessor {
        void processWord(String word);
    }
}
