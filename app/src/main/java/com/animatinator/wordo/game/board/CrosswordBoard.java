package com.animatinator.wordo.game.board;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.animatinator.wordo.util.Coordinates;

public class CrosswordBoard {
    private Coordinates topLeft = null;
    private Coordinates size = null;

    private Paint backgroundPaint;
    private Rect backgroundRect = new Rect(0, 0, 0, 0);

    public CrosswordBoard() {
        initPaints();
    }

    private void initPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GREEN);
    }

    public void updateLayout(Coordinates topLeft, Coordinates size) {
        this.topLeft = topLeft;
        this.size = size;
        updateBackgroundRect();
    }

    private void updateBackgroundRect() {
        int left = (int) topLeft.x();
        int top = (int) topLeft.y();
        int right = (int) (left + size.x());
        int bottom = (int) (top + size.y());
        backgroundRect = new Rect(left, top, right, bottom);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawRect(backgroundRect, backgroundPaint);
    }
}
