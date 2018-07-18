package com.animatinator.wordo.game.board;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.animatinator.wordo.prototype.PrototypeBoardLayout;
import com.animatinator.wordo.util.Coordinates;

import java.util.Optional;

public class CrosswordBoard {
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);

    private Coordinates topLeft = null;
    private Coordinates size = null;

    private Paint backgroundPaint;
    private Paint squarePaint;
    private Paint letterPaint;
    private Rect backgroundRect = new Rect(0, 0, 0, 0);

    private PrototypeBoardLayout fakeBoard;
    private float gridSize;
    private float textSize;

    public CrosswordBoard() {
        initFakeBoard();
        initPaints();
    }

    private void initFakeBoard() {
        fakeBoard = new PrototypeBoardLayout(5, 5);
        fakeBoard.addWord("CASE", new Coordinates(1, 1), PrototypeBoardLayout.Direction.HORIZONTAL);
        fakeBoard.addWord("CAUSE", new Coordinates(2, 0), PrototypeBoardLayout.Direction.VERTICAL);
        fakeBoard.addWord("SEA", new Coordinates(4, 0), PrototypeBoardLayout.Direction.VERTICAL);
        fakeBoard.addWord("ACED", new Coordinates(0, 4), PrototypeBoardLayout.Direction.HORIZONTAL);
    }

    private void initPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GREEN);

        squarePaint = new Paint();
        squarePaint.setColor(Color.BLACK);

        letterPaint = new Paint();
        letterPaint.setColor(Color.WHITE);
        letterPaint.setTextSize(textSize);
        letterPaint.setTypeface(TEXT_TYPEFACE);
    }

    public void maybeRevealWord(String word) {
        fakeBoard.maybeRevealWord(word);
    }

    public void updateLayout(Coordinates topLeft, Coordinates size) {
        this.topLeft = topLeft;
        this.size = size;
        updateBackgroundRect();
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
        Coordinates boardSize = fakeBoard.getSize();
        float longestSide = Math.max(boardSize.x(), boardSize.y());
        gridSize = size.x() / longestSide;
    }

    private void updateTextSizing() {
        textSize = gridSize;
        letterPaint.setTextSize(textSize);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawRect(backgroundRect, backgroundPaint);
        drawBoard(canvas);
    }

    @SuppressLint("NewApi")
    private void drawBoard(Canvas canvas) {
        Coordinates boardSize = fakeBoard.getSize();

        for (int y = 0; y < boardSize.y(); y++) {
            for (int x = 0; x < boardSize.x(); x++) {
                Optional<String> value = fakeBoard.getValueAt(x, y);
                if (value.isPresent()) {
                    float left = topLeft.x() + (x * gridSize);
                    float top = topLeft.y() + (y * gridSize);
                    float right = left + gridSize;
                    float bottom = top + gridSize;
                    canvas.drawRect(left, top, right, bottom, squarePaint);

                    if (fakeBoard.isRevealed(x, y)) {
                        String characterHere = value.get();
                        canvas.drawText(characterHere, left, top + gridSize, letterPaint);
                    }
                }
            }
        }
    }
}