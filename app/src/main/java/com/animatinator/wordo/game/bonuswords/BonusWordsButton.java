package com.animatinator.wordo.game.bonuswords;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.animatinator.wordo.R;
import com.animatinator.wordo.game.GameButton;
import com.animatinator.wordo.util.Coordinates;

public class BonusWordsButton extends GameButton {
    private static final Typeface TEXT_TYPEFACE = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    private BonusWordsRecord bonusWordsRecord;

    private Paint textPaint;
    private BonusWordsCallback callback = bonusWords -> {};

    public BonusWordsButton(Context context, BonusWordsRecord bonusWordsRecord) {
        super(context);
        this.bonusWordsRecord = bonusWordsRecord;
        initPaints();
    }

    private void initPaints() {
        textPaint = new Paint();
        int textColour = ContextCompat.getColor(context, R.color.textColor);
        textPaint.setColor(textColour);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(0.0f);
        textPaint.setTypeface(TEXT_TYPEFACE);
    }

    public void setBonusWordsRecord(BonusWordsRecord bonusWordsRecord) {
        this.bonusWordsRecord = bonusWordsRecord;
    }

    @Override
    public void updateLayout(Coordinates centre, float radius) {
        super.updateLayout(centre, radius);
        textPaint.setTextSize(radius);
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
