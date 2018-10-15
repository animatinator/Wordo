package com.animatinator.wordo.config;

// TODO: This shouldn't be static - use shared preferences or similar.
public class PuzzleGenerationConfig {
    private static Difficulty difficulty = Difficulty.NORMAL;
    private static PuzzleSize puzzleSize = PuzzleSize.MEDIUM;

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(Difficulty difficulty) {
        PuzzleGenerationConfig.difficulty = difficulty;
    }

    public static PuzzleSize getPuzzleSize() {
        return puzzleSize;
    }

    public static void setPuzzleSize(PuzzleSize puzzleSize) {
        PuzzleGenerationConfig.puzzleSize = puzzleSize;
    }
}
