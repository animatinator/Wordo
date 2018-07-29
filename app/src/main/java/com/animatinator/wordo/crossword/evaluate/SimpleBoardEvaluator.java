package com.animatinator.wordo.crossword.evaluate;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.board.BoardLayout;
import com.animatinator.wordo.crossword.generate.BoardGenerationFlagConstant;
import com.animatinator.wordo.crossword.generate.BoardGenerationFlags;
import com.animatinator.wordo.crossword.util.BoardLayoutUtils;

public class SimpleBoardEvaluator implements BoardEvaluator {
    private final BoardGenerationFlags flags;

    public SimpleBoardEvaluator(BoardGenerationFlags flags) {
        this.flags = flags;
    }

    @Override
    public double evaluateBoard(Board board) {
        BoardLayout layout = board.getLayout();
        double aspectRatio = getAspectRatio(layout);
        double longestSide = getLongestSide(layout);
        double numWords = board.getLaidWords().size();
        double intersectionCountFactor = getIntersectionCountFactor(layout);
        return (numWords * intersectionCountFactor) / (longestSide * aspectRatio);
    }

    private double getAspectRatio(BoardLayout layout) {
        double sizeRatio = (double)layout.getWidth() / (double)layout.getHeight();
        if (sizeRatio < 1.0) sizeRatio = (1.0d / sizeRatio);
        return sizeRatio;
    }

    private double getLongestSide(BoardLayout layout) {
        return Math.max(layout.getWidth(), layout.getHeight());
    }

    private double getIntersectionCountFactor(BoardLayout layout) {
        if (flags.getFlag(BoardGenerationFlagConstant.PREFER_MORE_INTERSECTIONS)) {
            return (BoardLayoutUtils.countIntersections(layout) + 1) / 10.0d;
        } else {
            return 1.0d;
        }
    }
}
