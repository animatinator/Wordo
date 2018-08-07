package com.animatinator.wordo.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.animatinator.wordo.crossword.CrosswordLayout;
import com.animatinator.wordo.game.board.CrosswordBoard;
import com.animatinator.wordo.game.bonuswords.BonusWordsButton;
import com.animatinator.wordo.game.bonuswords.BonusWordsCallback;
import com.animatinator.wordo.game.keyboard.RotaryKeyboard;
import com.animatinator.wordo.util.Coordinates;

public class GameView extends View implements View.OnTouchListener {
    // The ratio of the spacing around the board to the size of the board itself.
    private static final float BOARD_SPACING_RATIO = 1.1f;
    // The ratio of the spacing around the keyboard to the size of the keyboard itself.
    private static final float KEYBOARD_SPACING_RATIO = 1.1f;
    // The radius of buttons.
    private static final float BUTTON_RADIUS = 100.0f;
    // The ratio of the spacing around a button to the radius of the button itself.
    private static final float BUTTON_SPACING_RATIO = 1.3f;

    public static final String TAG = "GameView";

    private final BonusWordsButton bonusWordsButton;
    private final CrosswordBoard board;
    private final RotaryKeyboard keyboard;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        bonusWordsButton = new BonusWordsButton();
        board = new CrosswordBoard();
        keyboard = new RotaryKeyboard();
        keyboard.setWordEntryCallback(new RotaryKeyboard.WordEntryCallback() {
            @Override
            public void onWordEntered(String word) {
                Log.d(TAG, "===== Word entered: "+word+" =====");
                boolean revealed = board.maybeRevealWord(word);

                // If the word wasn't on the board, it might be a bonus word.
                if (!revealed) {
                    if (board.hasBonusWord(word)) {
                        Log.d(TAG, "===== Bonus word:" + word);
                        bonusWordsButton.addToRevealedWords(word);
                    }
                }
            }

            @Override
            public void onPartialWord(String partialWord) {
                // Do nothing.
            }
        });
    }

    public void setLetters(String[] letters) {
        keyboard.setLetters(letters);
    }

    public void setPuzzleLayout(CrosswordLayout layout) {
        board.setPuzzleLayout(layout);
    }

    public void setBonusWordsButtonCallback(BonusWordsCallback callback) {
        bonusWordsButton.setBonusWordsCallback(callback);
    }

    @Override
    public void onDraw(Canvas canvas) {
        board.onDraw(canvas);
        keyboard.onDraw(canvas);
        bonusWordsButton.onDraw(canvas);
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

        updateBonusButtonLayout(width, height);
    }

    private void updateBonusButtonLayout(int width, int height) {
        float buttonOffset = BUTTON_SPACING_RATIO * BUTTON_RADIUS;
        float x = width - buttonOffset;
        float y = height - buttonOffset;
        bonusWordsButton.updateLayout(new Coordinates(x, y), BUTTON_RADIUS);
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
            handled = bonusWordsButton.handleTouch(position) || keyboard.handlePress(position);
        } else if (action == MotionEvent.ACTION_MOVE) {
            keyboard.handleMovement(position);
            handled = true;
        }

        if (handled) invalidate();

        return handled;
    }
}
