package com.animatinator.wordo.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.animatinator.wordo.game.board.CrosswordBoard;
import com.animatinator.wordo.game.keyboard.RotaryKeyboard;
import com.animatinator.wordo.util.Coordinates;

public class GameView extends View implements View.OnTouchListener {
    // The ratio of the spacing around the board to the size of the board itself.
    private static final float BOARD_SPACING_RATIO = 1.2f;
    // The ratio of the spacing around the keyboard to the size of the keyboard itself.
    private static final float KEYBOARD_SPACING_RATIO = 1.1f;

    private final CrosswordBoard board;
    private final RotaryKeyboard keyboard;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        board = new CrosswordBoard();
        keyboard = new RotaryKeyboard();
    }

    public void setLetters(String[] letters) {
        keyboard.setLetters(letters);
    }

    public void setWordEntryCallback(RotaryKeyboard.WordEntryCallback wordEntryCallback) {
        keyboard.setWordEntryCallback(wordEntryCallback);
    }

    @Override
    public void onDraw(Canvas canvas) {
        board.onDraw(canvas);
        keyboard.onDraw(canvas);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            updateLayout(right - left, bottom - top);
        }
    }

    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        updateLayout(width, height);
    }

    private void updateLayout(int width, int height) {
        float centreX = width / 2.0f;

        float keyboardRadius = ((float)width) * 0.3f;
        Coordinates keyboardCentre =
                new Coordinates(
                        centreX, height - (keyboardRadius * KEYBOARD_SPACING_RATIO));
        float keyboardTop = height - ((keyboardRadius * KEYBOARD_SPACING_RATIO) * 2.0f);

        float boardSize = Math.min(width, keyboardTop) / BOARD_SPACING_RATIO;
        float boardX = centreX - (boardSize / 2.0f);
        float boardY = (keyboardTop / 2.0f) - (boardSize / 2.0f);

        keyboard.updateLayout(keyboardCentre, keyboardRadius);
        board.updateLayout(new Coordinates(boardX, boardY), new Coordinates(boardSize, boardSize));
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean handled = false;

        Coordinates position = new Coordinates(event.getX(), event.getY());
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            keyboard.handleRelease();
            handled = true;
        } else if (action == MotionEvent.ACTION_DOWN) {
            handled = keyboard.handlePress(position);
        } else if (action == MotionEvent.ACTION_MOVE) {
            keyboard.handleMovement(position);
            handled = true;
        }

        if (handled) invalidate();

        return handled;
    }
}
