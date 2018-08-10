package com.animatinator.wordo.game.hints;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.animatinator.wordo.game.GameButton;

public class HintButton extends GameButton {
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    private Paint textPaint;

    public HintButton() {
        super();
        initPaints();
    }

    private void initPaints() {
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(0.0f);
        textPaint.setTypeface(TEXT_TYPEFACE);
    }

    @Override
    protected void onPressed() {

    }

    @Override
    protected void drawButtonForeground(Canvas canvas) {
        drawCentredText(canvas, "?", textPaint);
    }
}
