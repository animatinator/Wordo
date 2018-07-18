package com.animatinator.wordo.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.animatinator.wordo.game.keyboard.RotaryKeyboard;
import com.animatinator.wordo.util.Coordinates;

public class GameView extends View implements View.OnTouchListener {
    private final RotaryKeyboard keyboard;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
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
        keyboard.onDraw(canvas);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            keyboard.updateSize(new Coordinates(right - left, bottom - top));
        }
    }

    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        keyboard.updateSize(new Coordinates(width, height));
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
