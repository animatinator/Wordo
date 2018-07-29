package com.animatinator.wordo.crossword.generate;

import android.util.SparseBooleanArray;

import java.util.HashMap;
import java.util.Map;

public class BoardGenerationFlags {
    private final SparseBooleanArray flagValues = new SparseBooleanArray();

    public BoardGenerationFlags() {
        initialiseFlagsToFalse();
        setInitialDefaults();
    }

    private void initialiseFlagsToFalse() {
        for (BoardGenerationFlagConstant value : BoardGenerationFlagConstant.values()) {
            setFlag(value, false);
        }
    }

    private void setInitialDefaults() {
        setFlag(BoardGenerationFlagConstant.RANDOM_INITIAL_ORIENTATION, true);
    }

    public void setFlag(BoardGenerationFlagConstant flag, boolean value) {
        flagValues.put(flag.ordinal(), value);
    }

    public boolean getFlag(BoardGenerationFlagConstant flag) {
        return flagValues.get(flag.ordinal());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{ ");
        for (BoardGenerationFlagConstant constant : BoardGenerationFlagConstant.values()) {
            if (getFlag(constant)) {
                builder.append(constant.name());
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
