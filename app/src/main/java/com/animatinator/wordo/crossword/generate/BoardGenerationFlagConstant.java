package com.animatinator.wordo.crossword.generate;

public enum BoardGenerationFlagConstant {
    // Randomise the orientation of the first word placed.
    RANDOM_INITIAL_ORIENTATION,
    // This flag doesn't control generation; we just use it to test the flags class.
    UNUSED_TEST_FLAG,
    // When laying a word, pick randomly from several of the top options rather than just the best.
    PICK_RANDOMLY_FROM_BEST_FEW_WORD_PLACEMENTS,
    // Generate a series of boards and pick the best one.
    GENERATE_SEVERAL_BOARDS,
    // Prefer boards with more intersections when evaluating.
    PREFER_MORE_INTERSECTIONS,
}
