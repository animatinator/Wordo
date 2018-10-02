package com.animatinator.wordo.game.keyboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.animatinator.wordo.game.util.TextDrawingUtils;
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
    private static final int TEXT_ANIMATION_DURATION_MS = 500;

    private Coordinates centre;
    private float height;
    private float cornerRadius;  // Radius of rounded corners on the background rect.
    private Paint backgroundPaint;
    private Paint wrongAnswerBackgroundPaint;
    private Paint rightAnswerBackgroundPaint;
    private Paint textPaint;
    private AnimationState animationState;

    private String enteredText = "";

    private enum AnimationType {
        NONE, WRONG_ANSWER_ANIMATION, RIGHT_ANSWER_ANIMATION
    }

    private class AnimationState {
        private AnimationType currentAnimation = AnimationType.NONE;
        private String textToAnimate = "";

        AnimationType getCurrentAnimation() {
            return currentAnimation;
        }

        public void setCurrentAnimation(AnimationType currentAnimation) {
            this.currentAnimation = currentAnimation;
        }

        String getTextToAnimate() {
            return textToAnimate;
        }

        void setTextToAnimate(String textToAnimate) {
            this.textToAnimate = textToAnimate;
        }

        boolean shouldContinueShowingText() {
            return currentAnimation != AnimationType.NONE;
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

            float textHeight = TextDrawingUtils.getTextHeight(textPaint);
            float textWidth = textPaint.measureText(textToUse);

            float rectWidth = textWidth / TEXT_SIZE_RATIO;
            float halfWidth = rectWidth / 2.0f;
            float halfHeight = height / 2.0f;

            canvas.drawRoundRect(
                    centre.x() - halfWidth,
                    centre.y() - halfHeight,
                    centre.x() + halfWidth,
                    centre.y() + halfHeight,
                    cornerRadius, cornerRadius,
                    paintToUse);

            canvas.drawText(textToUse, centre.x(), centre.y() - (textHeight / 2.0f), textPaint);
        }
    }

    public void updateEnteredText(String enteredText) {
        this.enteredText = enteredText;
    }

    public void notifyGuessCorrect(String guess) {
        animationState.setCurrentAnimation(AnimationType.RIGHT_ANSWER_ANIMATION);
        animationState.setTextToAnimate(guess);
        resetAnimationAfterDelay();
    }

    public void notifyGuessIncorrect(String guess) {
        animationState.setCurrentAnimation(AnimationType.WRONG_ANSWER_ANIMATION);
        animationState.setTextToAnimate(guess);
        resetAnimationAfterDelay();
    }

    private void resetAnimationAfterDelay() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                animationState.setCurrentAnimation(AnimationType.NONE);
            }
        }, TEXT_ANIMATION_DURATION_MS);
    }
}
