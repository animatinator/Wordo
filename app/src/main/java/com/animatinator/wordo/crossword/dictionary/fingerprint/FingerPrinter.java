package com.animatinator.wordo.crossword.dictionary.fingerprint;

import java.util.Arrays;

public class FingerPrinter {
    public static WordFingerPrint getFingerprint(String word) {
        if (word.length() == 0) {
            return new WordFingerPrint(new String[]{});
        }

        String[] characters = word.toLowerCase().split("");
        Arrays.sort(characters);
        return new WordFingerPrint(characters);
    }
}
