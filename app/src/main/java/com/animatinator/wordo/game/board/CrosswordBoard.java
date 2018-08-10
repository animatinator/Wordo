package com.animatinator.wordo.game.board;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.animatinator.wordo.crossword.CrosswordLayout;
import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;
import com.animatinator.wordo.crossword.util.Vector2d;
import com.animatinator.wordo.util.Coordinates;

import java.util.Optional;

public class CrosswordBoard {
    private static final Typeface TEXT_TYPEFACE =
            Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    private static final boolean DRAW_DEBUG_RECT = false;

    // topLeft is the top-left corner of this view area. drawnBoardTopLeft is the top-left corner of
    // the board being drawn, once it's been centred.
    private Coordinates topLeft = null;
    private Coordinates drawnBoardTopLeft = null;
    private Coordinates size = null;

    private Paint backgroundPaint;
    private Paint squarePaint;
    private Paint squareOutlinePaint;
    private Paint letterPaint;
    private Rect backgroundRect = new Rect(0, 0, 0, 0);

    private CrosswordLayout crosswordLayout;
    private float gridSize;
    private float textSize;

    // Keep track of whether we've already set up the layout params. If so, setting the puzzle or
    // similar will require recomputing the layout.
    private boolean layoutInitialised = false;

    public CrosswordBoard() {
        initFakeBoard();
        initPaints();
    }

    /**
     * Set it up with a test board at first. This also provides a neat fallback if we totally failed
     * to generate a board for whatever reason.
     */
    private void initFakeBoard() {
        crosswordLayout = new CrosswordLayout(5, 5);
        crosswordLayout.addWord(
                new LaidWord("CASE", new BoardPosition(1, 1), Direction.HORIZONTAL));
        crosswordLayout.addWord(
                new LaidWord("CAUSE", new BoardPosition(2, 0), Direction.VERTICAL));
        crosswordLayout.addWord(
                new LaidWord("SEA", new BoardPosition(4, 0), Direction.VERTICAL));
        crosswordLayout.addWord(
                new LaidWord("ACED", new BoardPosition(0, 4), Direction.HORIZONTAL));
    }

    private void initPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GREEN);

        squarePaint = new Paint();
        squarePaint.setColor(Color.LTGRAY);

        squareOutlinePaint = new Paint();
        squareOutlinePaint.setColor(Color.GRAY);
        squareOutlinePaint.setStyle(Paint.Style.STROKE);

        letterPaint = new Paint();
        letterPaint.setColor(Color.BLACK);
        letterPaint.setTextSize(textSize);
        letterPaint.setTypeface(TEXT_TYPEFACE);
    }

    public void setPuzzleLayout(CrosswordLayout layout) {
        crosswordLayout = layout;
        // If the layout was already set up earlier, we'll need to recompute various params now.
        if (layoutInitialised) {
            updateLayout(topLeft, size);
        }
    }

    public boolean maybeRevealWord(String word) {
        return crosswordLayout.maybeRevealWord(word);
    }

    public boolean hasBonusWord(String word) {
        return crosswordLayout.hasBonusWord(word);
    }

    public void giveHint() {
        crosswordLayout.giveHint();
    }

    public void updateLayout(Coordinates topLeft, Coordinates size) {
        this.topLeft = topLeft;
        this.size = size;
        layoutInitialised = true;
        if (DRAW_DEBUG_RECT) {
            updateBackgroundRect();
        }
        updateGrid();
        updateTextSizing();
    }

    private void updateBackgroundRect() {
        int left = (int) topLeft.x();
        int top = (int) topLeft.y();
        int right = (int) (left + size.x());
        int bottom = (int) (top + size.y());
        backgroundRect = new Rect(left, top, right, bottom);
    }

    private void updateGrid() {
        Vector2d boardSize = crosswordLayout.getSize();
        float longestSide = Math.max(boardSize.x(), boardSize.y());
        gridSize = size.x() / longestSide;
        if (boardSize.x() > boardSize.y()) {
            float drawnBoardHeight = boardSize.y() * gridSize;
            drawnBoardTopLeft = topLeft.withYOffset((size.y() - drawnBoardHeight) / 2.0f);
        } else {
            float drawnBoardWidth = boardSize.x() * gridSize;
            drawnBoardTopLeft = topLeft.withXOffset((size.x() - drawnBoardWidth) / 2.0f);
        }
    }

    private void updateTextSizing() {
        textSize = gridSize;
        letterPaint.setTextSize(textSize);
    }

    public void onDraw(Canvas canvas) {
        if (DRAW_DEBUG_RECT) {
            canvas.drawRect(backgroundRect, backgroundPaint);
        }
        drawBoard(canvas);
    }

    @SuppressLint("NewApi")
    private void drawBoard(Canvas canvas) {
        Vector2d boardSize = crosswordLayout.getSize();

        for (int y = 0; y < boardSize.y(); y++) {
            for (int x = 0; x < boardSize.x(); x++) {
                String value = crosswordLayout.getValueAt(x, y);
                if (value != null) {
                    float left = drawnBoardTopLeft.x() + (x * gridSize);
                    float top = drawnBoardTopLeft.y() + (y * gridSize);
                    float right = left + gridSize;
                    float bottom = top + gridSize;
                    canvas.drawRect(left, top, right, bottom, squarePaint);
                    canvas.drawRect(left, top, right, bottom, squareOutlinePaint);

                    if (crosswordLayout.isRevealed(x, y)) {
                        // Calculate offsets from the grid tile position to centre letters.
                        float letterHeight = letterPaint.ascent() + letterPaint.descent();
                        float yOffset = (gridSize - letterHeight) / 2.0f;
                        float letterWidth = letterPaint.measureText(value);
                        float xOffset = (gridSize - letterWidth) / 2.0f;
                        canvas.drawText(value, left + xOffset, top + yOffset, letterPaint);
                    }
                }
            }
        }
    }
}
