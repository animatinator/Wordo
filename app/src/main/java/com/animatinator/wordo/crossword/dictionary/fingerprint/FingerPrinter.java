package com.animatinator.wordo.crossword.dictionary.fingerprint;

import java.util.Arrays;

public class FingerPrinter {
    public static WordFingerPrint getFingerprint(String word) {
        if (word.length() == 0) {
            return new WordFingerPrint(new String[]{});
        }

        String[] characters = getLowerCaseCharacters(word);
        Arrays.sort(characters);
        return new WordFingerPrint(characters);
    }

    private static String[] getLowerCaseCharacters(String word) {
        String lowerCase = word.toLowerCase();
        String[] result = new String[word.length()];

        for (int i = 0; i < lowerCase.length(); i++) {
            result[i] = String.valueOf(lowerCase.charAt(i));
        }

        return result;
    }
}
