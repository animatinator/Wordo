package com.animatinator.wordo.game.keyboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.animatinator.wordo.game.util.TextDrawingUtils;
import com.animatinator.wordo.util.Coordinates;

/**
 * Shows the text being entered; updated from outside. If no text entered, displays nothing.
 */
public class EnteredTextDisplay {
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    // The ratio of the text size to the size of the text display.
    private static final float TEXT_SIZE_RATIO = 0.8f;

    private Coordinates centre;
    private float height;
    private float cornerRadius;  // Radius of rounded corners on the background rect.
    private Paint backgroundPaint;
    private Paint textPaint;

    private String enteredText = "";

    public EnteredTextDisplay() {
        this(new Coordinates(0, 0), 0.0f);
    }

    private EnteredTextDisplay(Coordinates centre, float height) {
        this.centre = centre;
        this.height = height;
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
        if (enteredText.length() > 0) {
            float textHeight = TextDrawingUtils.getTextHeight(textPaint);
            float textWidth = textPaint.measureText(enteredText);

            float rectWidth = textWidth / TEXT_SIZE_RATIO;
            float halfWidth = rectWidth / 2.0f;
            float halfHeight = height / 2.0f;
            canvas.drawRoundRect(
                    centre.x() - halfWidth,
                    centre.y() - halfHeight,
                    centre.x() + halfWidth,
                    centre.y() + halfHeight,
                    cornerRadius, cornerRadius,
                    backgroundPaint);

            canvas.drawText(enteredText, centre.x(), centre.y() - (textHeight / 2.0f), textPaint);
        }
    }

    public void updateEnteredText(String enteredText) {
        this.enteredText = enteredText;
    }
}
