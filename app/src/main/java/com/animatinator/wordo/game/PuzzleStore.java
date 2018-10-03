package com.animatinator.wordo.game;

import android.util.LongSparseArray;

import com.animatinator.wordo.crossword.PuzzleConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores generated puzzle configurations globally by ID. Used to conveniently share generated
 * layouts with the game activity without serialising and deserialising.
 */
public class PuzzleStore {
    private static long nextId = 0;
    private static LongSparseArray<PuzzleConfiguration> configMap = new LongSparseArray<>();

    public static long storePuzzleConfiguration(PuzzleConfiguration puzzleConfiguration) {
        long id = nextId++;
        configMap.put(id, puzzleConfiguration);
        return id;
    }

    public static PuzzleConfiguration loadPuzzleConfiguration(long id) {
        return configMap.get(id);
    }
}
