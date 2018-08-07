package com.animatinator.wordo.game.bonuswords;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.animatinator.wordo.util.CoordinateUtils;
import com.animatinator.wordo.util.Coordinates;

public class BonusWordsButton {
    // Size of the letters
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    private Coordinates centre;
    private float radius;
    private BonusWordsRecord bonusWordsRecord = new BonusWordsRecord();

    private Paint circlePaint;
    private Paint textPaint;
    private BonusWordsCallback callback = bonusWords -> {};

    public BonusWordsButton() {
        centre = new Coordinates(0.0f, 0.0f);
        radius = 0.0f;
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.LTGRAY);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
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
        canvas.drawCircle(centre.x(), centre.y(), radius, circlePaint);

        String revealedWords = ""+bonusWordsRecord.getNumberOfRevealedWords();

        float letterHeight = textPaint.ascent() + textPaint.descent();
        float yOffset = letterHeight / 2.0f;
        canvas.drawText(revealedWords, centre.x(), centre.y() - yOffset, textPaint);
    }

    public void setBonusWordsCallback(BonusWordsCallback callback) {
        this.callback = callback;
    }

    public boolean handleTouch(Coordinates position) {
        if (CoordinateUtils.distance(position, centre) < radius) {
            callback.showBonusWords(bonusWordsRecord.getRevealedWords());
            return true;
        }

        return false;
    }

}
