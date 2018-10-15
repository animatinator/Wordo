package com.animatinator.wordo.config;

public enum Difficulty {
    EASY,
    NORMAL,
    HARD {
        @Override
        public Difficulty next() {
            return EASY;
        }
    };

    public Difficulty next() {
        return values()[ordinal() + 1];
    }
}
