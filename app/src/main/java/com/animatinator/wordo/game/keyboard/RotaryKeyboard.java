package com.animatinator.wordo.game.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.animatinator.wordo.R;
import com.animatinator.wordo.game.util.Typefaces;
import com.animatinator.wordo.util.CoordinateUtils;
import com.animatinator.wordo.util.Coordinates;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class RotaryKeyboard {
    // The position along the circle's radius of the letters.
    // 0.0 = in the centre, 1.0 = on the edge.
    private static final float LETTER_RADIUS_RATIO = 0.75f;
    // Radius within which a click will initially count as hitting a letter.
    private static final float LETTER_INITIAL_HIT_RADIUS = 100f;
    // Radius within which a motion event (whilst dragging) will count as hitting a letter.
    private static final float LETTER_DRAG_HIT_RADIUS = 70f;
    // Radius of the circle drawn around a letter when it is selected, relative to the radius of the
    // circle.
    private static final float LETTER_HIGHLIGHT_RADIUS_RADIO = 0.2f;
    // Size of the letters
    private static final float TEXT_SIZE = 100.0f;

    private final Context context;

    private WordEntryCallback wordEntryCallback = null;

    private Paint linePaint;
    private Paint letterHighlightPaint;
    private Paint circlePaint;
    private Paint textPaint;
    private Paint highlightedTextPaint;

    private Coordinates centre = null;
    private float radius = 10.0f;

    private String[] letters = null;
    private Coordinates[] letterPositions = null;

    private boolean isDragging = false;
    private Coordinates currentPos = new Coordinates(0, 0);
    private List<Integer> selectedLetters = new CopyOnWriteArrayList<>();

    public RotaryKeyboard(Context context) {
        this.context = context;
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        int circleColour = ContextCompat.getColor(context, R.color.rotaryKeyboardBackground);
        circlePaint.setColor(circleColour);

        int traceColour = ContextCompat.getColor(context, R.color.rotaryKeyboardTrace);

        linePaint = new Paint();
        linePaint.setColor(traceColour);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(50.0f);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        letterHighlightPaint = new Paint();
        letterHighlightPaint.setColor(traceColour);
        letterHighlightPaint.setStyle(Paint.Style.FILL);

        Typeface textTypeface = Typefaces.getBoldTypeface(context);

        textPaint = new Paint();
        int textColour = ContextCompat.getColor(context, R.color.textDark);
        textPaint.setColor(textColour);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTypeface(textTypeface);

        highlightedTextPaint = new Paint();
        int textHighlightColour = ContextCompat.getColor(context, R.color.textLight);
        highlightedTextPaint.setColor(textHighlightColour);
        highlightedTextPaint.setTextAlign(Paint.Align.CENTER);
        highlightedTextPaint.setTextSize(TEXT_SIZE);
        highlightedTextPaint.setTypeface(textTypeface);
    }

    public void updateLayout(Coordinates centre, float radius) {
        this.centre = centre;
        this.radius = radius;
        recomputeLetterPositions();
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
        recomputeLetterPositions();
    }

    public void setWordEntryCallback(WordEntryCallback wordEntryCallback) {
        this.wordEntryCallback = wordEntryCallback;
    }

    @SuppressLint("NewApi")
    public boolean handlePress(Coordinates position) {
        Optional<Integer> selectedLetter = getAdjacentLetter(position, LETTER_INITIAL_HIT_RADIUS);
        if (selectedLetter.isPresent()) {
            isDragging = true;
            currentPos = new Coordinates(position.x(), position.y());
            selectedLetters = new CopyOnWriteArrayList<>();
            selectedLetters.add(selectedLetter.get());
            return true;
        }
        return false;
    }

    public void handleRelease() {
        if (isDragging) {
            wordEntryCallback.onWordEntered(getEnteredWord());
            wordEntryCallback.onPartialWord("");
            isDragging = false;
            selectedLetters = new CopyOnWriteArrayList<>();
        }
    }

    @SuppressLint("NewApi")
    public void handleMovement(Coordinates position) {
        Optional<Integer> selectedLetter = getAdjacentLetter(position, LETTER_DRAG_HIT_RADIUS);
        if (selectedLetter.isPresent()) {
            if (selectedLetters.contains(selectedLetter.get())) {
                stripSelectedLettersBackToLetter(selectedLetter.get());
            } else {
                selectedLetters.add(selectedLetter.get());
            }
            wordEntryCallback.onPartialWord(getEnteredWord());
        }
        currentPos = position;
    }

    @SuppressLint("NewApi")
    private Optional<Integer> getAdjacentLetter(Coordinates position, float hitRadius) {
        for (int i = 0; i < letterPositions.length; i++) {
            Coordinates letterPosition = letterPositions[i];
            if (CoordinateUtils.distance(letterPosition, position) < hitRadius) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }

    public void onDraw(Canvas canvas) {
        Coordinates circleCentre = centre;
        canvas.drawCircle(circleCentre.x(), circleCentre.y(), radius, circlePaint);

        if (isDragging) {
            drawSelectedLetterTrail(canvas);
        }

        drawLetters(canvas);
    }

    private void drawSelectedLetterTrail(Canvas canvas) {
        if (selectedLetters == null) {
            return;
        }

        // TODO we're assuming that the letters and positions can't be null here.

        Coordinates lastLetterPosition = null;

        for (Integer letterIndex : selectedLetters) {
            Coordinates letterPosition = letterPositions[letterIndex];

            if (lastLetterPosition != null) {
                canvas.drawLine(
                        lastLetterPosition.x(),
                        lastLetterPosition.y(),
                        letterPosition.x(),
                        letterPosition.y(),
                        linePaint);
            }

            canvas.drawCircle(
                    letterPosition.x(), letterPosition.y(), LETTER_HIGHLIGHT_RADIUS_RADIO * radius, letterHighlightPaint);
            lastLetterPosition = letterPosition;
        }

        if (lastLetterPosition != null) {
            canvas.drawLine(lastLetterPosition.x(), lastLetterPosition.y(), currentPos.x(), currentPos.y(), linePaint);
        }
    }

    private void drawLetters(Canvas canvas) {
        if (letters == null || letterPositions == null) {
            return;
        }

        for (int i = 0; i < letters.length; i++) {
            String letter = letters[i];
            Coordinates position = letterPositions[i];
            // Centre vertically.
            float yPos = position.y() - ((textPaint.ascent() + textPaint.descent()) / 2.0f);

            // Choose whether to use the highlighted text or normal text paint based on whether the
            // current letter is in the list of selected letters.
            Paint paintToUse;

            if (selectedLetters.contains(i)) {
                paintToUse = highlightedTextPaint;
            } else {
                paintToUse = textPaint;
            }

            canvas.drawText(letter, position.x(), yPos, paintToUse);
        }
    }

    private void recomputeLetterPositions() {
        if (letters != null && centre != null) {
            int numLetters = letters.length;

            letterPositions = new Coordinates[numLetters];

            float letterDistanceFromCentre = radius * LETTER_RADIUS_RATIO;

            for (int i = 0; i < numLetters; i++) {
                double angle = (Math.PI * 2 * i) / numLetters;

                float xFact = (float) Math.sin(angle);
                float yFact = (float) Math.cos(angle);
                float xPos = centre.x() + (letterDistanceFromCentre * xFact);
                float yPos = centre.y() - (letterDistanceFromCentre * yFact);

                letterPositions[i] = new Coordinates(xPos, yPos);
            }
        }
    }

    private String getEnteredWord() {
        StringBuilder builder = new StringBuilder();
        for (Integer id : selectedLetters) {
            builder.append(letters[id]);
        }
        return builder.toString();
    }

    private void stripSelectedLettersBackToLetter(Integer selectedLetter) {
        int index = selectedLetters.indexOf(selectedLetter);
        selectedLetters = selectedLetters.subList(0, index + 1);
    }

}
