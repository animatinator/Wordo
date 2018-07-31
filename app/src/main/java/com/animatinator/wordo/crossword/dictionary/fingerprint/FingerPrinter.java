package com.animatinator.wordo.crossword.dictionary.fingerprint;

import java.util.Arrays;

public class FingerPrinter {
    public static WordFingerPrint getFingerprint(String word) {
        if (word.length() == 0) {
            return new WordFingerPrint(new String[]{});
        }

        String[] characters = getCharacters(word);
        Arrays.sort(characters);
        return new WordFingerPrint(characters);
    }

    private static String[] getCharacters(String word) {
        String[] result = new String[word.length()];

        for (int i = 0; i < word.length(); i++) {
            result[i] = String.valueOf(word.charAt(i));
        }

        return result;
    }
}
