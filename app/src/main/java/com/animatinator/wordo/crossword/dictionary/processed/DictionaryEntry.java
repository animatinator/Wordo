package com.animatinator.wordo.crossword.dictionary.processed;

import com.animatinator.wordo.crossword.dictionary.fingerprint.WordFingerPrint;

import java.util.Objects;

public class DictionaryEntry {
    private String word;
    private WordFingerPrint fingerPrint;

    public DictionaryEntry(String word, WordFingerPrint fingerPrint) {
        this.word = word;
        this.fingerPrint = fingerPrint;
    }

    public String word() {
        return word;
    }

    public WordFingerPrint fingerPrint() {
        return fingerPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryEntry that = (DictionaryEntry) o;
        return Objects.equals(word, that.word) &&
                Objects.equals(fingerPrint, that.fingerPrint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, fingerPrint);
    }
}
