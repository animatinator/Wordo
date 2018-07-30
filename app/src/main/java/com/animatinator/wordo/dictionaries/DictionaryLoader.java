package com.animatinator.wordo.dictionaries;

import android.content.Context;
import android.content.res.AssetManager;

import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DictionaryLoader {
    private AssetManager assetManager;

    DictionaryLoader(Context context) {
        assetManager = context.getAssets();
    }

    public ProcessedDictionary loadDictionary(String name) throws IOException {
        ProcessedDictionary result = new ProcessedDictionary();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(name)));

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            if (currentLine.startsWith("#")) {
                continue;
            }
            result.addWord(currentLine);
        }

        return result;
    }
}
