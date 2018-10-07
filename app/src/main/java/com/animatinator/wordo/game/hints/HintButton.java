package com.animatinator.wordo.game.hints;

import android.content.Context;
import android.graphics.Canvas;

import com.animatinator.wordo.game.GameButton;

public class HintButton extends GameButton {
    private HintButtonCallback callback;

    public HintButton(Context context) {
        super(context);
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
