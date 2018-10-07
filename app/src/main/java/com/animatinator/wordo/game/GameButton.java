package com.animatinator.wordo.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.animatinator.wordo.R;
import com.animatinator.wordo.game.util.Typefaces;
import com.animatinator.wordo.util.CoordinateUtils;
import com.animatinator.wordo.util.Coordinates;

public abstract class GameButton {
    protected final Context context;

    protected Coordinates centre;
    protected float radius;

    protected Paint textPaint;
    private Paint circlePaint;
    private Rect textBounds = new Rect();

    public GameButton(Context context) {
        this.context = context;
        centre = new Coordinates(0.0f, 0.0f);
        radius = 0.0f;
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        int backgroundColour = ContextCompat.getColor(context, R.color.buttonBackground);
        circlePaint.setColor(backgroundColour);

        textPaint = new Paint();
        Typeface textTypeface = Typefaces.getBoldTypeface(context);
        int textColour = ContextCompat.getColor(context, R.color.textDark);
        textPaint.setColor(textColour);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(0.0f);
        textPaint.setTypeface(textTypeface);
    }

    public void updateLayout(Coordinates centre, float radius) {
        this.centre = centre;
        this.radius = radius;
        textPaint.setTextSize(radius);
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
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, centre.x(), centre.y() - textBounds.exactCenterY(), textPaint);
    }

    protected abstract void onPressed();

    protected abstract void drawButtonForeground(Canvas canvas);
}
