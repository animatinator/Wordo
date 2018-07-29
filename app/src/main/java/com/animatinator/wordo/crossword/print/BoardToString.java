package com.animatinator.wordo.crossword.print;

import com.animatinator.wordo.crossword.board.Board;

/**
 * Converts a {@link Board} into a string representation.
 */
public interface BoardToString {
    String getStringRepresentation(Board board);
}
