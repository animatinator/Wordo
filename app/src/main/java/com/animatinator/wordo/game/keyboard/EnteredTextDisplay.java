package com.animatinator.wordo.game.keyboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.animatinator.wordo.util.Coordinates;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Shows the text being entered; updated from outside. If no text entered, displays nothing.
 */
public class EnteredTextDisplay {
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    // The ratio of the text size to the size of the text display.
    private static final float TEXT_SIZE_RATIO = 0.8f;
    // The duration of animations.
    private static final int TEXT_ANIMATION_DURATION_MS = 300;
    // The amplitude of the shaking animation for wrong guesses.
    private static final float SHAKE_AMPLITUDE = 30.0f;
    // The period of the shaking animation for wrong guesses.
    private static final double SHAKE_PERIOD_MS = 20;

    private Coordinates centre;
    private float height;
    private float cornerRadius;  // Radius of rounded corners on the background rect.
    private Paint backgroundPaint;
    private Paint wrongAnswerBackgroundPaint;
    private Paint rightAnswerBackgroundPaint;
    private Paint textPaint;
    private AnimationState animationState;

    private String enteredText = "";
    private Rect textBounds = new Rect();

    private enum AnimationType {
        NONE, WRONG_ANSWER_ANIMATION, RIGHT_ANSWER_ANIMATION
    }

    private class AnimationState {
        private AnimationType currentAnimation = AnimationType.NONE;
        private String textToAnimate = "";
        private long startTime;

        AnimationState() {
            startTime = System.currentTimeMillis();
        }

        AnimationType getCurrentAnimation() {
            return currentAnimation;
        }

        String getTextToAnimate() {
            return textToAnimate;
        }

        boolean shouldContinueShowingText() {
            return currentAnimation != AnimationType.NONE;
        }

        void startAnimation(AnimationType animationType, String text) {
            currentAnimation = animationType;
            textToAnimate = text;
            startTime = System.currentTimeMillis();
        }

        void stopAnimation() {
            currentAnimation = AnimationType.NONE;
        }

        long getStartTime() {
            return startTime;
        }
    }

    public EnteredTextDisplay() {
        this(new Coordinates(0, 0), 0.0f);
    }

    private EnteredTextDisplay(Coordinates centre, float height) {
        this.centre = centre;
        this.height = height;
        this.animationState = new AnimationState();
        initPaints();
        updateTextSize();
    }

    private void initPaints() {
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(TEXT_TYPEFACE);

        backgroundPaint = new Paint();
        backgroundPaint.setARGB(255, 0, 0, 150);
        backgroundPaint.setStyle(Paint.Style.FILL);

        rightAnswerBackgroundPaint = new Paint();
        rightAnswerBackgroundPaint.setARGB(255, 0, 150, 0);
        rightAnswerBackgroundPaint.setStyle(Paint.Style.FILL);

        wrongAnswerBackgroundPaint = new Paint();
        wrongAnswerBackgroundPaint.setARGB(255, 150, 0, 0);
        wrongAnswerBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void updateTextSize() {
        textPaint.setTextSize(height * TEXT_SIZE_RATIO);
    }

    public void updateLayout(Coordinates centre, float height) {
        this.centre = centre;
        this.height = height;
        cornerRadius = height / 2.0f;
        updateTextSize();
    }

    public void onDraw(Canvas canvas) {
        // Only draw if we have text.
        if (enteredText.length() > 0 || animationState.shouldContinueShowingText()) {

            Paint paintToUse = backgroundPaint;
            String textToUse = enteredText;

            if (animationState.getCurrentAnimation() != AnimationType.NONE) {
                textToUse = animationState.getTextToAnimate();
            }

            switch (animationState.getCurrentAnimation()) {
                case WRONG_ANSWER_ANIMATION:
                    paintToUse = wrongAnswerBackgroundPaint;
                    break;
                case RIGHT_ANSWER_ANIMATION:
                    paintToUse = rightAnswerBackgroundPaint;
                    break;
                default:
                    break;
            }

            float animatedCentreX = centre.x();

            if (animationState.getCurrentAnimation() == AnimationType.WRONG_ANSWER_ANIMATION) {
                long curTime = System.currentTimeMillis();
                long timeDiff = curTime - animationState.getStartTime();
                float xOffset =
                        ((float)Math.sin(((double)timeDiff) / SHAKE_PERIOD_MS)) * SHAKE_AMPLITUDE;
                animatedCentreX = animatedCentreX + xOffset;
            }

            textPaint.getTextBounds(textToUse, 0, textToUse.length(), textBounds);

            // Give the rect the same horizontal padding as it has vertical (don't scale it with the
            // length of the string).
            float rectPadding = height - textBounds.height();
            float rectWidth = textBounds.width() + rectPadding;

            float halfRectWidth = rectWidth / 2.0f;
            float halfRectHeight = height / 2.0f;

            canvas.drawRoundRect(
                    animatedCentreX - halfRectWidth,
                    centre.y() - halfRectHeight,
                    animatedCentreX + halfRectWidth,
                    centre.y() + halfRectHeight,
                    cornerRadius, cornerRadius,
                    paintToUse);

            canvas.drawText(textToUse, animatedCentreX, centre.y() - textBounds.exactCenterY(), textPaint);
        }
    }

    public void updateEnteredText(String enteredText) {
        this.enteredText = enteredText;
    }

    public void notifyGuessCorrect(String guess) {
        animationState.startAnimation(AnimationType.RIGHT_ANSWER_ANIMATION, guess);
        resetAnimationAfterDelay();
    }

    public void notifyGuessIncorrect(String guess) {
        animationState.startAnimation(AnimationType.WRONG_ANSWER_ANIMATION, guess);
        resetAnimationAfterDelay();
    }

    private void resetAnimationAfterDelay() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                animationState.stopAnimation();
            }
        }, TEXT_ANIMATION_DURATION_MS);
    }
}
