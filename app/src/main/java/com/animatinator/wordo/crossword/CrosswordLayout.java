package com.animatinator.wordo.crossword;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.board.BoardLayout;
import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.dictionary.processed.ProcessedDictionary;
import com.animatinator.wordo.crossword.util.BoardOffset;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;
import com.animatinator.wordo.crossword.util.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * A board layout for use in-game. This stores a list of laid words and a 2D array of board tiles,
 * and manages revealing words as the game progresses.
 */
public class CrosswordLayout {

    private static final String TAG = "CrosswordLayout";

    private final Random random = new Random();
    private final BoardTile[][] board;
    private final Vector2d size;
    private final ProcessedDictionary dictionary;

    private Map<String, LaidWord> laidWords;

    public CrosswordLayout(int width, int height) {
        board = new BoardTile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = null;
            }
        }
        size = calculateSize();
        laidWords = new HashMap<>();
        dictionary = new ProcessedDictionary();
    }

    CrosswordLayout(Board generatedBoard, ProcessedDictionary dictionary) {
        this.dictionary = dictionary;
        BoardLayout layout = generatedBoard.getLayout();
        board = createBoardLayoutFromExistingLayout(layout);
        size = calculateSize();

        // Because the existing board may not have its top-left corner at (0, 0), we have to take
        // the real position of its top-left corner into account and subtract it from all LaidWord
        // positions.
        BoardOffset laidWordOffset = new BoardOffset(layout.getTopLeft()).negative();
        laidWords = createLaidWordsMapFromExistingBoard(generatedBoard, laidWordOffset);
    }

    private BoardTile[][] createBoardLayoutFromExistingLayout(BoardLayout existingLayout) {
        int width = existingLayout.getWidth();
        int height = existingLayout.getHeight();

        BoardTile[][] layout = new BoardTile[height][width];
        BoardPosition topLeft = existingLayout.getTopLeft();

        for (int y = 0; y < height; y++) {
            for (int x =0; x < width; x++) {
                Optional<String> valueHere = existingLayout.getAt(new BoardPosition(x + topLeft.x(), y + topLeft.y()));
                layout[y][x] = valueHere.map(BoardTile::new).orElse(null);
            }
        }

        return layout;
    }

    private Map<String, LaidWord> createLaidWordsMapFromExistingBoard(Board board, BoardOffset topLeftOffset) {
        Map<String, LaidWord> laidWordsMap = new HashMap<>();

        for (LaidWord word : board.getLaidWords()) {
            LaidWord adjustedWord =
                    new LaidWord(
                            word.getWord(),
                            word.getTopLeft().withOffset(topLeftOffset),
                            word.getDirection());
            if (laidWordsMap.containsKey(word.getWord())) {
                Log.e(TAG, "Duplicate word: "+word.getWord());
            }
            laidWordsMap.put(word.getWord(), adjustedWord);
        }

        return laidWordsMap;
    }

    private Vector2d calculateSize() {
        return new Vector2d(board[0].length, board.length);
    }

    // Only used for testing, so package-private.
    Map<String, LaidWord> getLaidWordsMapForTesting() {
        return laidWords;
    }

    public void addWord(LaidWord word) {
        String wordString = word.getWord();
        BoardPosition position = word.getTopLeft();

        laidWords.put(wordString, word);
        if (word.getDirection() == Direction.VERTICAL) {
            for (int y = 0; y < wordString.length(); y++) {
                board[y + position.y()][position.x()] = new BoardTile(""+wordString.charAt(y));
            }
        } else {
            for (int x = 0; x < wordString.length(); x++) {
                board[position.y()][x + position.x()] = new BoardTile(""+ wordString.charAt(x));
            }
        }
    }

    public Vector2d getSize() {
        return size;
    }

    @SuppressLint("NewApi")
    @Nullable
    public String getValueAt(int x, int y) {
        checkInRange(x, y);
        BoardTile tile = board[y][x];
        if (tile != null) {
            return tile.getValue();
        }
        return null;
    }

    public boolean isRevealed(int x, int y) {
        checkInRange(x, y);
        BoardTile tile = board[y][x];
        return tile != null && tile.isRevealed();
    }

    private void checkInRange(int x, int y) {
        Vector2d size = getSize();
        if (x < 0 || y < 0 || x >= size.x() || y >= size.y()) {
            throw new IllegalArgumentException("Accessing a position out of range!");
        }
    }

    public boolean maybeRevealWord(String word) {
        Log.i("CrosswordLayout", "maybeRevealWord: "+word);
        boolean revealed = false;

        LaidWord laidWord = laidWords.get(word);
        if (laidWord != null) {
            Log.i("CrosswordLayout", "Revealing word: "+word);
            revealed = true;

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

        return revealed;
    }

    public boolean hasBonusWord(String word) {
        return dictionary.isWordInFullDictionary(word);
    }

    public void giveHint() {
        List<BoardTile> possibleReveals = new ArrayList<>();

        for (BoardTile[] row : board) {
            for (BoardTile tile : row) {
                if (tile != null && !tile.isRevealed()) {
                    possibleReveals.add(tile);
                }
            }
        }

        if (possibleReveals.size() == 0) {
            // Nothing left to reveal!
            return;
        }

        int indexToReveal = random.nextInt(possibleReveals.size());
        possibleReveals.get(indexToReveal).reveal();
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
