package com.animatinator.wordo.game.hints;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.animatinator.wordo.R;
import com.animatinator.wordo.game.GameButton;

public class HintButton extends GameButton {
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    private Paint textPaint;
    private HintButtonCallback callback;

    public HintButton(Context context) {
        super(context);
        initPaints();
    }

    private void initPaints() {
        textPaint = new Paint();
        int textColour = ContextCompat.getColor(context, R.color.textDark);
        textPaint.setColor(textColour);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(100.0f);
        textPaint.setTypeface(TEXT_TYPEFACE);
    }

    public void setCallback(HintButtonCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPressed() {
        callback.requestHint();
    }

    @Override
    protected void drawButtonForeground(Canvas canvas) {
        drawCentredText(canvas, "?", textPaint);
    }
}
