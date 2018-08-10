package com.animatinator.wordo.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.animatinator.wordo.util.CoordinateUtils;
import com.animatinator.wordo.util.Coordinates;

public abstract class GameButton {
    protected Coordinates centre;
    protected float radius;

    private Paint circlePaint;

    public GameButton() {
        centre = new Coordinates(0.0f, 0.0f);
        radius = 0.0f;
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.LTGRAY);
    }

    public void updateLayout(Coordinates centre, float radius) {
        this.centre = centre;
        this.radius = radius;
    }

    public boolean handleTouch(Coordinates position) {
        if (CoordinateUtils.distance(position, centre) < radius) {
            onPressed();
            return true;
        }

        return false;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawCircle(centre.x(), centre.y(), radius, circlePaint);
        drawButtonForeground(canvas);
    }

    protected void drawCentredText(Canvas canvas, String text, Paint textPaint) {
        float letterHeight = textPaint.ascent() + textPaint.descent();
        float yOffset = letterHeight / 2.0f;
        canvas.drawText(text, centre.x(), centre.y() - yOffset, textPaint);
    }

    protected abstract void onPressed();

    protected abstract void drawButtonForeground(Canvas canvas);
}
