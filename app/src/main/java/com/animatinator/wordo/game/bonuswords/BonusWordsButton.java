package com.animatinator.wordo.game.bonuswords;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.animatinator.wordo.util.Coordinates;

public class BonusWordsButton {
    // Size of the letters
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    private Coordinates centre;
    private float radius;
    private BonusWordsRecord bonusWordsRecord = new BonusWordsRecord();

    private Paint circlePaint;
    private Paint textPaint;

    public BonusWordsButton() {
        centre = new Coordinates(0.0f, 0.0f);
        radius = 0.0f;
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(0.0f);
        textPaint.setTypeface(TEXT_TYPEFACE);
    }

    public void updateLayout(Coordinates centre, float radius) {
        this.centre = centre;
        this.radius = radius;
        textPaint.setTextSize(radius);
    }

    public void addToRevealedWords(String word) {
        bonusWordsRecord.revealBonusWord(word);
    }

    public void onDraw(Canvas canvas) {
        int revealedWords = bonusWordsRecord.getNumberOfRevealedWords();

        canvas.drawCircle(centre.x(), centre.y(), radius, circlePaint);
        canvas.drawText(""+revealedWords, centre.x(), centre.y(), textPaint);
    }
}
