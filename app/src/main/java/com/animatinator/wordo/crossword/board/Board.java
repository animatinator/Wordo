package com.animatinator.wordo.crossword.board;

import com.animatinator.wordo.crossword.board.words.LaidWord;
import com.animatinator.wordo.crossword.board.words.WordIntersections;
import com.animatinator.wordo.crossword.util.BoardPosition;
import com.animatinator.wordo.crossword.util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Board {
    private List<LaidWord> laidWords = new ArrayList<>();
    // A list of words which should have gone on the board but could not be fitted in.
    private final List<String> unlaidWords = new ArrayList<>();
    private final WordIntersections intersectionDetector = new WordIntersections();

    public Board() {}

    public Board(Board existing) {
        laidWords = new ArrayList<>(existing.laidWords);
    }

    public void addWord(String word, BoardPosition position, Direction direction) {
        LaidWord wordToLay = new LaidWord(word, position, direction);
        if (canWordBeAdded(wordToLay)) {
            laidWords.add(wordToLay);
        } else {
            throw new IllegalArgumentException("Can't add word here! It intersects incorrectly with an existing word.");
        }
    }

    public void addUnlaidWord(String word) {
        unlaidWords.add(word);
    }

    public List<String> getUnlaidWords() {
        return unlaidWords;
    }

    public List<LaidWord> getPossibleAttachmentPointsForWord(String wordToAdd) {
        List<LaidWord> attachmentPoints = new ArrayList<>();
        HashSet<String> lettersInNewWord = new HashSet<>(Arrays.asList(wordToAdd.split("")));
        BoardLayout currentBoardLayout = getLayout();

        for (LaidWord laidWord : laidWords) {
            List<String> letters = laidWord.getCharacters();

            for (int i = 0; i < laidWord.getLength(); i++) {
                String currentLetter = letters.get(i);
                if (lettersInNewWord.contains(currentLetter)) {
                    BoardPosition newWordPosition;

                    // This is the index within the to-be-added word of its intersection with the existing word to which
                    // it's being attached.
                    int wordIntersectionIndex = wordToAdd.indexOf(currentLetter.charAt(0));

                    // TODO: This only adds matches for the first instance of a letter in a word.
                    if (laidWord.getDirection() == Direction.VERTICAL) {
                        newWordPosition = new BoardPosition(
                                laidWord.getTopLeft().x() - wordIntersectionIndex,
                                laidWord.getTopLeft().y() + i);
                    } else {
                        newWordPosition = new BoardPosition(
                                laidWord.getTopLeft().x() + i,
                                laidWord.getTopLeft().y() - wordIntersectionIndex);
                    }

                    Direction direction =
                            (laidWord.getDirection() == Direction.VERTICAL) ? Direction.HORIZONTAL : Direction.VERTICAL;

                    LaidWord possibleNewLaidWord = new LaidWord(wordToAdd, new BoardPosition(newWordPosition), direction);

                    if (canWordBeAdded(possibleNewLaidWord)) {
                        if (!currentBoardLayout.isAdjacentToExistingWord(possibleNewLaidWord)) {
                            attachmentPoints.add(possibleNewLaidWord);
                        }
                    }
                }
            }
        }

        return attachmentPoints;
    }

    public List<LaidWord> getLaidWords() {
        return laidWords;
    }

    public BoardLayout getLayout() {
        return recomputeLayout();
    }

    Boundaries getBoundaries() {
        if (laidWords.isEmpty()) {
            return new Boundaries(new BoardPosition(0, 0), new BoardPosition(0, 0));
        }

        int left = Integer.MAX_VALUE, right = -Integer.MAX_VALUE, top = Integer.MAX_VALUE, bottom = -Integer.MAX_VALUE;

        for (LaidWord word : laidWords) {
            left = min(left, word.getTopLeft().x());
            right = max(right, word.getBottomRight().x());
            top = min(top, word.getTopLeft().y());
            bottom = max(bottom, word.getBottomRight().y());
        }

        return new Boundaries(new BoardPosition(left, top), new BoardPosition(right, bottom));
    }

    private BoardLayout recomputeLayout() {
        Boundaries boundaries = getBoundaries();
        BoardLayout layout = new BoardLayout(boundaries.getWidth(), boundaries.getHeight(), boundaries.getTopLeft());

        for (LaidWord word : laidWords) {
            addWordToLayout(word, layout);
        }

        return layout;
    }

    private void addWordToLayout(LaidWord word, BoardLayout layout) {
        List<String> characters = word.getCharacters();
        BoardPosition startPos = word.getTopLeft();
        int xDirection = (word.getDirection() == Direction.HORIZONTAL) ? 1 : 0;
        int yDirection = (word.getDirection() == Direction.VERTICAL) ? 1 : 0;

        for (int i = 0; i < word.getLength(); i++) {
            BoardPosition current = new BoardPosition(startPos.x() + xDirection * i, startPos.y() + yDirection * i);
            Optional<String> valueAtCurrent = layout.getAt(current);

            if (valueAtCurrent.isPresent()) {
                // Throw if this word clashes. We should have prevented this elsewhere though.
                if (!valueAtCurrent.get().equals(characters.get(i))) {
                    throw new IllegalArgumentException("Can't add word '"+ word.getWord()+"' because it clashes on character "+characters.get(i));
                }
            }

            layout.setTile(current, characters.get(i));
        }
    }

    private boolean canWordBeAdded(LaidWord wordToLay) {
        return laidWords.stream().noneMatch(
                laidWord -> intersectionDetector.wordsIntersectIllegally(wordToLay, laidWord));
    }
}
