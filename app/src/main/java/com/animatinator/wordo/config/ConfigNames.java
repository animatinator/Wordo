package com.animatinator.wordo.config;

import android.content.Context;

import com.animatinator.wordo.R;

public final class ConfigNames {
    public static String getStringForDifficulty(Context context, Difficulty difficulty) {
        int id = 0;

        switch (difficulty) {
            case EASY:
                id = R.string.difficulty_easy;
                break;
            case NORMAL:
                id = R.string.difficulty_normal;
                break;
            case HARD:
                id = R.string.difficulty_hard;
                break;
            default:
                break;
        }

        return context.getResources().getString(id);
    }

    public static String getStringForPuzzleSize(Context context, PuzzleSize size) {
        int id = 0;

        switch (size) {
            case SMALL:
                id = R.string.puzzle_size_small;
                break;
            case MEDIUM:
                id = R.string.puzzle_size_medium;
                break;
            case LARGE:
                id = R.string.puzzle_size_large;
                break;
            case ENORMOUS:
                id = R.string.puzzle_size_enormous;
                break;
            default:
                break;
        }

        return context.getResources().getString(id);
    }
}
