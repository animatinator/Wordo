package com.animatinator.wordo.prototype;

import android.annotation.SuppressLint;
import android.util.Log;

import com.animatinator.wordo.util.Coordinates;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class is a minimal piece of untested nonsense very loosely based on code her:
 * https://github.com/animatinator/experimental/tree/master/crossword.
 * It's only here to prototype drawing and word revealing code before fully integrating the puzzle
 * generator.
 *
 * TODO: remove this and implement properly.
 */
public class PrototypeBoardLayout {

    private BoardTile[][] board;
    private Map<String, LaidWord> laidWords;

    public PrototypeBoardLayout(int width, int height) {
        board = new BoardTile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = null;
            }
        }
        laidWords = new HashMap<>();
    }

    public void addWord(String word, Coordinates position, Direction direction) {
        laidWords.put(word, new LaidWord(position, direction));
        if (direction == Direction.VERTICAL) {
            for (int y = 0; y < word.length(); y++) {
                board[y + (int)position.y()][(int)position.x()] = new BoardTile(""+word.charAt(y));
            }
        } else {
            for (int x = 0; x < word.length(); x++) {
                board[(int)position.y()][x + (int)position.x()] = new BoardTile(""+ word.charAt(x));
            }
        }
    }

    public Coordinates getSize() {
        return new Coordinates(board[0].length, board.length);
    }

    @SuppressLint("NewApi")
    public Optional<String> getValueAt(int x, int y) {
        BoardTile tile = board[y][x];
        if (tile != null) {
            return Optional.of(tile.getValue());
        }
        return Optional.empty();
    }

    public boolean isRevealed(int x, int y) {
        BoardTile tile = board[y][x];

        return tile != null && tile.isRevealed();

    }

    public void maybeRevealWord(String word) {
        Log.i("PrototypeBoardLayout", "maybeRevealWord: "+word);
        LaidWord laidWord = laidWords.get(word);
        if (laidWord != null) {
            Log.i("PrototypeBoardLayout", "Revealing word: "+word);
            if (laidWord.direction == Direction.VERTICAL) {
                for (int y = 0; y < word.length(); y++) {
                    board[y + (int)laidWord.getPosition().y()][(int)laidWord.getPosition().x()].reveal();
                }
            } else {
                for (int x = 0; x < word.length(); x++) {
                    board[(int)laidWord.getPosition().y()][x + (int)laidWord.getPosition().x()].reveal();
                }
            }
        }
    }

    public enum Direction {
        VERTICAL, HORIZONTAL
    }

    class BoardTile {
        private String value;
        private boolean revealed = false;

        BoardTile(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }

        boolean isRevealed() {
            return revealed;
        }

        void reveal() {
            revealed = true;
        }
    }

    class LaidWord {
        private final Coordinates position;
        private final Direction direction;

        LaidWord(Coordinates position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        Coordinates getPosition() {
            return position;
        }

        Direction getDirection() {
            return direction;
        }
    }
}
