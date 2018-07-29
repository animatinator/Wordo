package com.animatinator.wordo.crossword.dictionary.fingerprint;

import android.text.TextUtils;

import java.util.Arrays;

public class WordFingerPrint {
    private String[] characters;

    public WordFingerPrint(String[] characters) {
        this.characters = characters;
    }

    public String[] getCharacters() {
        return characters;
    }

    @Override public String toString() {
        return TextUtils.join("", characters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordFingerPrint that = (WordFingerPrint) o;
        return Arrays.equals(characters, that.characters);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(characters);
    }
}
