package com.animatinator.wordo.crossword.board.words;

import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LaidWord {
    private final String word;
    private final BoardPosition position;
    private final Direction direction;

    public LaidWord(String word, BoardPosition position, Direction direction) {
        this.word = word;
        this.position = position;
        this.direction = direction;
    }

    public String getWord() {
        return word;
    }

    public int getLength() {
        return word.length();
    }

    public BoardPosition getTopLeft() {
        return position;
    }

    public BoardPosition getBottomRight() {
        int xOffset = (direction == Direction.HORIZONTAL) ? (getLength() - 1) : 0;
        int yOffset = (direction == Direction.VERTICAL) ? (getLength() - 1) : 0;

        return getTopLeft().withXOffset(xOffset).withYOffset(yOffset);
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * It should be possible to use Arrays.asList(word.split("")), but that mysteriously fails in
     * ways I couldn't reproduce in tests. Sometimes (and only sometimes, and only when running on
     * the device), this would return N+1 results with the first being an empty string.
     */
    public List<String> getCharacters() {
        List<String> chars = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            chars.add(String.valueOf(word.charAt(i)));
        }
        return chars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaidWord laidWord = (LaidWord) o;
        return Objects.equals(word, laidWord.word) &&
                Objects.equals(position, laidWord.position) &&
                direction == laidWord.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, position, direction);
    }

    @Override
    public String toString() {
        return "LaidWord{" +
                "word='" + word + '\'' +
                ", position=" + position +
                ", direction=" + direction +
                '}';
    }
}
