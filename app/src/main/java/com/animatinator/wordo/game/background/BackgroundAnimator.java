package com.animatinator.wordo.game.background;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import com.animatinator.wordo.R;

/**
 * Draws an animated background over the whole {@link Canvas}.
 */
public class BackgroundAnimator {
    private static final float GRID_SPACING = 50.0f;
    // Number of grid lines per dark line.
    private static final int LINES_BETWEEN_DARK_LINES = 4;
    private static final float DARK_LINE_SPACING = LINES_BETWEEN_DARK_LINES * GRID_SPACING;
    private static final float GRID_SPEED = 1.0f;

    private final Context context;

    private Paint backgroundPaint;
    private Paint gridPaint;
    private Paint gridDarkPaint;

    private float gridX = 0.0f;
    private float gridY = 0.0f;

    public BackgroundAnimator(Context context) {
        this.context = context;
        initPaints();
    }

    private void initPaints() {
        backgroundPaint = new Paint();
        int backgroundColour = ContextCompat.getColor(context, R.color.gameBackground);
        backgroundPaint.setColor(backgroundColour);
        backgroundPaint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint();
        int gridColour = ContextCompat.getColor(context, R.color.gameBackgroundGrid);
        gridPaint.setColor(gridColour);

        gridDarkPaint = new Paint();
        int gridDarkColour = ContextCompat.getColor(context, R.color.gameBackgroundGridDark);
        gridPaint.setColor(gridDarkColour);
    }

    public void update() {
        // Reset the values each time we get back to where we started.
        gridX = (gridX + GRID_SPEED) % DARK_LINE_SPACING;
        gridY = (gridY + GRID_SPEED) % DARK_LINE_SPACING;
    }

    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

        int lineIndex = 0;

        for (float x = gridX - DARK_LINE_SPACING; x < width; x += GRID_SPACING) {
            Paint paintToUse = (lineIndex == 0) ? gridDarkPaint : gridPaint;

            canvas.drawLine(x, 0, x, height, paintToUse);

            lineIndex = (lineIndex + 1) % LINES_BETWEEN_DARK_LINES;
        }

        lineIndex = 0;

        for (float y = gridY - DARK_LINE_SPACING; y < height; y += GRID_SPACING) {
            Paint paintToUse = (lineIndex == 0) ? gridDarkPaint : gridPaint;

            canvas.drawLine(0, y, width, y, paintToUse);

            lineIndex = (lineIndex + 1) % LINES_BETWEEN_DARK_LINES;
        }
    }
}
