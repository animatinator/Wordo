package com.animatinator.wordo.game.keyboard;

/**
 * Used to listen to words being entered and submitted on the keyboard.
 */
public interface WordEntryCallback {
    void onWordEntered(String word);
    void onPartialWord(String partialWord);
}
