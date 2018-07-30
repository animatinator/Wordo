package com.animatinator.wordo.prototype;

import android.annotation.SuppressLint;
import android.util.Log;

import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;
import com.animatinator.wordo.crossword.util.Vector2d;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A board layout for use in-game. This stores a list of laid words and a 2D array of board tiles,
 * and manages revealing words as the game progresses.
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

    public void addWord(String word, BoardPosition position, Direction direction) {
        laidWords.put(word, new LaidWord(word, position, direction));
        if (direction == Direction.VERTICAL) {
            for (int y = 0; y < word.length(); y++) {
                board[y + position.y()][position.x()] = new BoardTile(""+word.charAt(y));
            }
        } else {
            for (int x = 0; x < word.length(); x++) {
                board[position.y()][x + position.x()] = new BoardTile(""+ word.charAt(x));
            }
        }
    }

    public Vector2d getSize() {
        return new Vector2d(board[0].length, board.length);
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
            if (laidWord.getDirection() == Direction.VERTICAL) {
                for (int y = 0; y < word.length(); y++) {
                    board[y + laidWord.getTopLeft().y()][laidWord.getTopLeft().x()].reveal();
                }
            } else {
                for (int x = 0; x < word.length(); x++) {
                    board[laidWord.getTopLeft().y()][x + laidWord.getTopLeft().x()].reveal();
                }
            }
        }
    }

    /**
     * Represents a single tile on the board.
     *
     * This contains the letter represented and whether or not it has been made visible yet.
     */
    private class BoardTile {
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
}
