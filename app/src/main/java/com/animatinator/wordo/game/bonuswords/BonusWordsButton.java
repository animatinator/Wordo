package com.animatinator.wordo.game.bonuswords;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.animatinator.wordo.game.GameButton;
import com.animatinator.wordo.util.Coordinates;

public class BonusWordsButton extends GameButton {
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    private BonusWordsRecord bonusWordsRecord = new BonusWordsRecord();

    private Paint textPaint;
    private BonusWordsCallback callback = bonusWords -> {};

    public BonusWordsButton() {
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
    public void updateLayout(Coordinates centre, float radius) {
        super.updateLayout(centre, radius);
        textPaint.setTextSize(radius);
    }

    public void addToRevealedWords(String word) {
        bonusWordsRecord.revealBonusWord(word);
    }

    @Override
    public void drawButtonForeground(Canvas canvas) {
        String revealedWords = ""+bonusWordsRecord.getNumberOfRevealedWords();

        drawCentredText(canvas, revealedWords, textPaint);
    }

    public void setBonusWordsCallback(BonusWordsCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPressed() {
        callback.showBonusWords(bonusWordsRecord.getRevealedWords());
    }

}
