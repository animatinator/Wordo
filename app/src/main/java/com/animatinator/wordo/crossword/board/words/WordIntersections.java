package com.animatinator.wordo.crossword.board.words;


import com.animatinator.wordo.crossword.util.Direction;

public class WordIntersections {
    /**
     * Decide whether two {@link LaidWord}s intersect illegally. 'Illegally' means they don't match on the letter at the
     * point of intersection - that is, we explicitly allow intersections like this:
     *
     * .t..
     * test
     * .s..
     * .t..
     *
     * However, overlaps on the same axis are not allowed, eg. the bottom composition of 'test' and 'steer':
     *
     * .........
     * .testeer.
     * .........
     */
    public boolean wordsIntersectIllegally(LaidWord first, LaidWord second) {
        if (first.getDirection() == Direction.HORIZONTAL
                && second.getDirection() == Direction.HORIZONTAL) {
            // Disable the IDE's preference for 'simplification' as it would render this unreadable.
            //noinspection SimplifiableIfStatement
            if (first.getTopLeft().y() != second.getTopLeft().y()) {
                // Horizontal words only intersect if they're at the same Y-position.
                return false;
            } else {
                return (first.getBottomRight().x() >= second.getTopLeft().x()
                        && first.getTopLeft().x() <= second.getBottomRight().x());
            }
        }

        if (first.getDirection() == Direction.VERTICAL
                && second.getDirection() == Direction.VERTICAL) {
            // Disable the IDE's preference for 'simplification' as it would render this unreadable.
            //noinspection SimplifiableIfStatement
            if (first.getTopLeft().x() != second.getTopLeft().x()) {
                // Vertical words only intersect if they're at the same X-position.
                return false;
            } else {
                return (first.getBottomRight().y() >= second.getTopLeft().y()
                        && first.getTopLeft().y() <= second.getBottomRight().y());
            }
        }

        return perpendicularWordsIntersect(first, second);
    }

    private boolean perpendicularWordsIntersect(LaidWord first, LaidWord second) {
        if ((first.getDirection() == second.getDirection())) {
            throw new AssertionError("These words are not perpendicular!");
        }

        LaidWord horizontal, vertical;
        if (first.getDirection() == Direction.HORIZONTAL) {
            horizontal = first;
            vertical = second;
        } else {
            vertical = first;
            horizontal = second;
        }

        int verticalX = vertical.getTopLeft().x();
        int horizontalY = horizontal.getTopLeft().y();

        if ((verticalX >= horizontal.getTopLeft().x() && verticalX <= horizontal.getBottomRight().x())
                && (horizontalY >= vertical.getTopLeft().y() && horizontalY <= vertical.getBottomRight().y())) {
            String horizontalLetter = horizontal.getCharacters().get(verticalX - horizontal.getTopLeft().x());
            String verticalLetter = vertical.getCharacters().get(horizontalY - vertical.getTopLeft().y());
            return !horizontalLetter.equals(verticalLetter);
        }

        return false;
    }
}
