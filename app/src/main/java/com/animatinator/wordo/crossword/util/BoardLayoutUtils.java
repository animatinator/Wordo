package com.animatinator.wordo.crossword.util;

import com.animatinator.wordo.crossword.board.BoardLayout;

public class BoardLayoutUtils {
    public static int countIntersections(BoardLayout layout) {
        int intersectionCount = 0;

        for (int y = 0; y < layout.getHeight(); y++) {
            for (int x = 0; x < layout.getWidth(); x++) {
                if (hasIntersection(layout, new BoardPosition(x, y))) {
                    intersectionCount++;
                }
            }
        }

        return intersectionCount;
    }

    private static boolean hasIntersection(BoardLayout layout, BoardPosition position) {
        BoardPosition adjustedPosition = position.withOffset(new BoardOffset(layout.getTopLeft()));

        if (layout.getAt(adjustedPosition).isPresent()) {
            boolean adjacentLeft = position.x() > 0 && layout.getAt(adjustedPosition.withXOffset(-1)).isPresent();
            boolean adjacentRight = position.x() < (layout.getWidth() - 1) && layout.getAt(adjustedPosition.withXOffset(1)).isPresent();
            boolean adjacentTop = position.y() > 0 && layout.getAt(adjustedPosition.withYOffset(-1)).isPresent();
            boolean adjacentBottom = position.y() < (layout.getHeight() - 1) && layout.getAt(adjustedPosition.withYOffset(1)).isPresent();

            return ((adjacentLeft || adjacentRight) && (adjacentTop || adjacentBottom));
        }

        return false;
    }
}