package com.animatinator.wordo.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.animatinator.wordo.crossword.CrosswordLayout;
import com.animatinator.wordo.game.board.CrosswordBoard;
import com.animatinator.wordo.game.bonuswords.BonusWordsButton;
import com.animatinator.wordo.game.bonuswords.BonusWordsCallback;
import com.animatinator.wordo.game.hints.HintButton;
import com.animatinator.wordo.game.keyboard.EnteredTextDisplay;
import com.animatinator.wordo.game.keyboard.RotaryKeyboard;
import com.animatinator.wordo.game.keyboard.WordEntryCallback;
import com.animatinator.wordo.util.Coordinates;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    // The ratio of the spacing around the board to the size of the board itself.
    private static final float BOARD_SPACING_RATIO = 1.1f;
    // The ratio of the spacing around the keyboard to the size of the keyboard itself.
    private static final float KEYBOARD_SPACING_RATIO = 1.1f;
    // The radius of buttons.
    private static final float BUTTON_RADIUS = 100.0f;
    // The ratio of the spacing around a button to the radius of the button itself.
    private static final float BUTTON_SPACING_RATIO = 1.3f;
    // Size of the 'entered word' display.
    private static final float ENTERED_WORD_DISPLAY_HEIGHT = 100.0f;
    // Padding to add around the 'entered word' display.
    private static final float ENTERED_WORD_DISPLAY_PADDING_RATIO = 1.1f;

    public static final String TAG = "GameView";

    private final BonusWordsButton bonusWordsButton;
    private final EnteredTextDisplay enteredTextDisplay;
    private final HintButton hintButton;
    private final CrosswordBoard board;
    private final RotaryKeyboard keyboard;

    private GameThread gameThread;
    private Paint backgroundPaint;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        bonusWordsButton = new BonusWordsButton();
        enteredTextDisplay = new EnteredTextDisplay();
        hintButton = new HintButton();
        board = new CrosswordBoard();
        keyboard = new RotaryKeyboard();
        keyboard.setWordEntryCallback(new WordEntryCallback() {
            @Override
            public void onWordEntered(String word) {
                Log.d(TAG, "===== Word entered: "+word+" =====");
                boolean revealed = board.maybeRevealWord(word);
                boolean bonusWord = false;

                // If the word wasn't on the board, it might be a bonus word.
                if (!revealed) {
                    if (board.hasBonusWord(word)) {
                        Log.d(TAG, "===== Bonus word:" + word);
                        bonusWordsButton.addToRevealedWords(word);
                        bonusWord = true;
                    }
                }

                if (revealed || bonusWord) {
                    enteredTextDisplay.notifyGuessCorrect(word);
                } else {
                    enteredTextDisplay.notifyGuessIncorrect(word);
                }
            }

            @Override
            public void onPartialWord(String partialWord) {
                enteredTextDisplay.updateEnteredText(partialWord);
            }
        });
        hintButton.setCallback(board::giveHint);
        gameThread = new GameThread(getHolder(), this);
        getHolder().addCallback(this);

        initPaints();
    }

    private void initPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.WHITE);
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

    public void drawToCanvas(Canvas canvas) {
        board.onDraw(canvas);
        keyboard.onDraw(canvas);
        bonusWordsButton.onDraw(canvas);
        hintButton.onDraw(canvas);
        enteredTextDisplay.onDraw(canvas);
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

        // Keyboard at the bottom.
        float keyboardRadius = ((float)width) * 0.3f;
        Coordinates keyboardCentre =
                new Coordinates(
                        centreX, height - (keyboardRadius * KEYBOARD_SPACING_RATIO));
        float keyboardTop = height - ((keyboardRadius * KEYBOARD_SPACING_RATIO) * 2.0f);

        // Entered text display right above it.
        float enteredTextHeight = ENTERED_WORD_DISPLAY_HEIGHT * ENTERED_WORD_DISPLAY_PADDING_RATIO;
        float enteredTextCentreY = keyboardTop - (enteredTextHeight / 2.0f);
        float enteredTextTop = keyboardTop - enteredTextHeight;

        // Board occupies the remaining space at the top.
        float boardSize = Math.min(width, enteredTextTop) / BOARD_SPACING_RATIO;
        float boardX = centreX - (boardSize / 2.0f);
        float boardY = (enteredTextTop / 2.0f) - (boardSize / 2.0f);

        enteredTextDisplay.updateLayout(new Coordinates(centreX, enteredTextCentreY), enteredTextHeight);
        keyboard.updateLayout(keyboardCentre, keyboardRadius);
        board.updateLayout(new Coordinates(boardX, boardY), new Coordinates(boardSize, boardSize));

        updateButtonLayouts(width, height);
    }

    private void updateButtonLayouts(int width, int height) {
        float buttonOffset = BUTTON_SPACING_RATIO * BUTTON_RADIUS;
        float x = width - buttonOffset;
        float y = height - buttonOffset;
        bonusWordsButton.updateLayout(new Coordinates(x, y), BUTTON_RADIUS);
        hintButton.updateLayout(new Coordinates(buttonOffset, y), BUTTON_RADIUS);
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
            handled = handlePossibleButtonTouch(position) || keyboard.handlePress(position);
        } else if (action == MotionEvent.ACTION_MOVE) {
            keyboard.handleMovement(position);
            handled = true;
        }

        if (handled) invalidate();

        return handled;
    }

    private boolean handlePossibleButtonTouch(Coordinates position) {
        return bonusWordsButton.handleTouch(position) || hintButton.handleTouch(position);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!gameThread.isRunning()) {
            gameThread = new GameThread(getHolder(), this);
        }
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        updateLayout(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        gameThread.setIsRunning(false);
    }

    private final class GameThread extends Thread {
        // 60fps maximum.
        private static final int FRAME_DELAY_MS = 1000 / 60;

        private final SurfaceHolder surfaceHolder;
        private final GameView gameView;

        private AtomicBoolean isRunning = new AtomicBoolean(false);

        GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        @Override
        public void run() {
            isRunning.set(true);

            while(isRunning.get()) {
                Canvas canvas = surfaceHolder.lockCanvas();

                if (canvas != null) {
                    synchronized (surfaceHolder) {
                        try {
                            canvas.drawRect(0, 0,
                                    canvas.getWidth(), canvas.getHeight(), backgroundPaint);

                            gameView.drawToCanvas(canvas);
                        } finally {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }

                try {
                    Thread.sleep(FRAME_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning.set(false);
                }
            }
        }

        synchronized void setIsRunning(boolean running) {
            isRunning.set(running);
        }

        synchronized boolean isRunning() {
            return isRunning.get();
        }
    }
}
