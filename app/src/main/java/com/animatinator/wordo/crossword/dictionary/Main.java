package com.animatinator.wordo.crossword.dictionary;

import android.annotation.SuppressLint;

import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;
import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleWordConfiguration;
import com.animatinator.wordo.crossword.dictionary.puzzle.PuzzleGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        ProcessedDictionary dictionary;
        try {
            dictionary = loadDictionaryFromFile(Paths.get(args[0]));
        } catch (IOException e) {
            System.out.println("Failed to load dictionary! Exception: "+e);
            e.printStackTrace();
            return;
        }
        PuzzleGenerator generator = new PuzzleGenerator(dictionary).withMinimumWordLength(3).withMaximumWordCount(10);
        PuzzleWordConfiguration puzzle = generator.buildPuzzle(7);
        System.out.println(puzzle);
    }

    private static ProcessedDictionary loadDictionaryFromFile(Path path) throws IOException {
        String[] words = loadWordsFromFile(path);
        return buildDictionaryFromWords(words);
    }

    // TODO: This doesn't work the same way on Android. Decide how we're storing and loading dictionaries.
    @SuppressLint("NewApi")
    private static String[] loadWordsFromFile(Path path) throws IOException {
        String rawDictionary = new String(Files.readAllBytes(path));
        return rawDictionary.split(System.getProperty("line.separator"));
    }

    private static ProcessedDictionary buildDictionaryFromWords(String[] words) {
        ProcessedDictionary dictionary = new ProcessedDictionary();

        for (String word : words) {
            // '#' lines are comments.
            if (word.startsWith("#")) {
                continue;
            }

            dictionary.addWord(word);
        }

        return dictionary;
    }
}
