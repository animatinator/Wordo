package com.animatinator.wordo.game.bonuswords;

import android.content.Context;
import android.graphics.Canvas;

import com.animatinator.wordo.game.GameButton;

public class BonusWordsButton extends GameButton {
    private BonusWordsRecord bonusWordsRecord;
    private BonusWordsCallback callback = bonusWords -> {};

    public BonusWordsButton(Context context, BonusWordsRecord bonusWordsRecord) {
        super(context);
        this.bonusWordsRecord = bonusWordsRecord;
    }

    public void setBonusWordsRecord(BonusWordsRecord bonusWordsRecord) {
        this.bonusWordsRecord = bonusWordsRecord;
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
