package com.animatinator.wordo.crossword.board;

import android.support.annotation.Nullable;

import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.util.BoardOffset;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;
import com.animatinator.wordo.crossword.util.Vector2d;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class BoardLayout {
    public static final String EMPTY_SPACE = ".";

    private int width;
    private int height;
    private BoardPosition topLeft;
    private BoardTile[][] layout;

    /**
     * @param width   The width of the board
     * @param height  The height of the board
     * @param topLeft The co-ordinates of the top-left tile of the board, to allow for negative locations
     */
    public BoardLayout(int width, int height, BoardPosition topLeft) {
        this.width = width;
        this.height = height;
        this.topLeft = topLeft;
        layout = createEmptyLayout(width, height);
    }

    /**
     * Constructs a {@link BoardLayout} with its top-left corner at (0, 0).
     *
     * @param width  The width of the board
     * @param height The height of the board
     */
    public BoardLayout(int width, int height) {
        this(width, height, new BoardPosition(0, 0));
    }

    private BoardTile[][] createEmptyLayout(int width, int height) {
        BoardTile[][] emptyLayout = new BoardTile[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                emptyLayout[y][x] = new BoardTile();
            }
        }

        return emptyLayout;
    }

    void setTile(BoardPosition position, String value) {
        PositionAdjustedBoardPosition adjustedPosition = new PositionAdjustedBoardPosition(position, topLeft);
        valueAt(adjustedPosition).setValue(value);
    }

    public BoardPosition getTopLeft() {
        return topLeft;
    }

    public Optional<String> getAt(BoardPosition position) {
        PositionAdjustedBoardPosition adjustedPosition = new PositionAdjustedBoardPosition(position, topLeft);

        if (!isOnBoard(adjustedPosition)) {
            throw new IllegalArgumentException("Requesting a position which isn't on the board!");
        }
        return valueAt(adjustedPosition).getValue();
    }

    // TODO test this!
    boolean isAdjacentToExistingWord(LaidWord possibleWord) {
        BoardOffset parallel, perpendicular;

        if (possibleWord.getDirection() == Direction.HORIZONTAL) {
            parallel = new BoardOffset(1, 0);
            perpendicular = new BoardOffset(0, 1);
        } else {
            parallel = new BoardOffset(0, 1);
            perpendicular = new BoardOffset(1, 0);
        }

        if (holdsLetter(possibleWord.getTopLeft().withOffset(parallel.negative()))) {
            return true;
        } else if (holdsLetter(possibleWord.getBottomRight().withOffset(parallel))) {
            return true;
        }

        for (int i = 0; i < possibleWord.getLength(); i++) {
            BoardPosition positionOfLetter = possibleWord.getTopLeft().withOffset(parallel.multiply(i));
            // If this point along the word isn't on top of an existing word (ie. this isn't a point of intersection
            // with an existing word), then we check the tiles on either side of it. If either of them is on top of an
            // existing word, the word we're trying to place lies adjacent to an existing word.
            if (!holdsLetter(positionOfLetter)) {
                if (holdsLetter(positionOfLetter.withOffset(perpendicular))
                        || holdsLetter(positionOfLetter.withOffset(perpendicular.negative()))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean holdsLetter(BoardPosition boardPosition) {
        PositionAdjustedBoardPosition adjustedPosition = new PositionAdjustedBoardPosition(boardPosition, topLeft);
        return isOnBoard(adjustedPosition) && valueAt(adjustedPosition).getValue().isPresent();
    }

    private boolean isOnBoard(PositionAdjustedBoardPosition position) {
        return position.x() >= 0 && position.x() < width && position.y() >= 0 && position.y() < height;
    }

    private BoardTile valueAt(PositionAdjustedBoardPosition position) {
        return layout[position.y()][position.x()];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // For testing.
    public void copyLayoutFromStringArray(String[][] stringArray) {
        if (stringArray.length == 0) {
            throw new IllegalArgumentException("Layout array cannot be empty");
        }
        height = stringArray.length;
        width = stringArray[0].length;

        layout = createLayoutArrayFromStringArray(stringArray);
    }

    private BoardTile[][] createLayoutArrayFromStringArray(String[][] stringArray) {
        int newHeight = stringArray.length;
        int newWidth = stringArray[0].length;

        BoardTile[][] newLayout = createEmptyLayout(newWidth, newHeight);

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                String value = stringArray[y][x];
                if (!value.equals(EMPTY_SPACE)) {
                    newLayout[y][x].setValue(value);
                }
            }
        }

        return newLayout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardLayout that = (BoardLayout) o;
        return width == that.width &&
                height == that.height &&
                // TODO: should compare toplefts?
                Arrays.deepEquals(layout, that.layout);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height);
        result = 31 * result + Arrays.hashCode(layout);
        return result;
    }

    /**
     * Tracks the value of an individual tile. Tracks whether the tile is an intersection between two words so we can
     * more easily check validities of new intersections.
     */
    private static final class BoardTile {
        @Nullable
        private String value;

        void setValue(@Nullable String value) {
            this.value = value;
        }

        Optional<String> getValue() {
            return Optional.ofNullable(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BoardTile boardTile = (BoardTile) o;
            // We ignore whether or not this is an intersection because they may not have been computed for test layouts
            // (see copyLayoutFromStringArray). This is unimportant because intersections are an emergent property of
            // layouts, and the boolean here is just a way of keeping track of them internally.
            return Objects.equals(value, boardTile.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    static final class PositionAdjustedBoardPosition extends Vector2d {
        PositionAdjustedBoardPosition(BoardPosition basePosition, BoardPosition topLeft) {
            super(basePosition.withXOffset(-topLeft.x()).withYOffset(-topLeft.y()));
        }

        @Override
        public String toString() {
            return String.format(Locale.ENGLISH, "PositionAdjustedBoardPosition(%d, %d)", x, y);
        }
    }
}
