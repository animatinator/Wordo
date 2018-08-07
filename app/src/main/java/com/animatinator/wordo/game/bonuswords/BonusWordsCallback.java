package com.animatinator.wordo.game.bonuswords;

import java.util.List;

/**
 * Used to listen for the 'bonus words' button asking to show the list of bonus words found so far.
 */
public interface BonusWordsCallback {
    void showBonusWords(List<String> bonusWords);
}
