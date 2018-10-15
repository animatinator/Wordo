package com.animatinator.wordo.config;

public enum PuzzleSize {
    SMALL,
    MEDIUM,
    LARGE,
    ENORMOUS {
        @Override
        public PuzzleSize next() {
            return SMALL;
        }
    };

    public PuzzleSize next() {
        return values()[ordinal() + 1];
    }
}
