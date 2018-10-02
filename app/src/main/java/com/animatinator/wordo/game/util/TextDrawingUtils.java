package com.animatinator.wordo.game.util;

import android.graphics.Paint;

public class TextDrawingUtils {
    public static float getTextHeight(Paint textPaint) {
        return textPaint.ascent() + textPaint.descent();
    }
}
