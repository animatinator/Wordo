package com.animatinator.wordo.config;

import android.content.Context;

import com.animatinator.wordo.R;

public class ConfigValues {

    public static int getWordLengthFromDifficulty(Context context, Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return context.getResources().getInteger(R.integer.puzzle_word_length_easy);
            case NORMAL:
                return context.getResources().getInteger(R.integer.puzzle_word_length_normal);
            case HARD:
                return context.getResources().getInteger(R.integer.puzzle_word_length_hard);
            default:
                return context.getResources().getInteger(R.integer.puzzle_word_length_normal);
        }
    }

    public static int getRealSizeFromPuzzleSize(Context context, PuzzleSize puzzleSize) {
        switch (puzzleSize) {
            case SMALL:
                return context.getResources().getInteger(R.integer.puzzle_size_small);
            case MEDIUM:
                return context.getResources().getInteger(R.integer.puzzle_size_medium);
            case LARGE:
                return context.getResources().getInteger(R.integer.puzzle_size_large);
            case ENORMOUS:
                return context.getResources().getInteger(R.integer.puzzle_size_enormous);
            default:
                return context.getResources().getInteger(R.integer.puzzle_size_medium);
        }
    }
}
