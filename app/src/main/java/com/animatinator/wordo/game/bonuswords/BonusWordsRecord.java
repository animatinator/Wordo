package com.animatinator.wordo.game.bonuswords;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BonusWordsRecord {
    private final HashSet<String> bonusWords = new HashSet<>();

    public void revealBonusWord(String word) {
        bonusWords.add(word);
    }

    public int getNumberOfRevealedWords() {
        return bonusWords.size();
    }

    public List<String> getRevealedWords() {
        return new ArrayList<>(bonusWords);
    }
}
