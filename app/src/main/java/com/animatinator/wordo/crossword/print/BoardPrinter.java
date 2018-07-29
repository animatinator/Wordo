package com.animatinator.wordo.crossword.print;

import com.animatinator.wordo.crossword.board.Board;

public class BoardPrinter {

    private final StringOutput output;
    private final BoardToString boardToString;

    public BoardPrinter(StringOutput output, BoardToString boardConverter) {
        this.output = output;
        boardToString = boardConverter;
    }

    public void printBoard(Board board) {
        String boardAsString = boardToString.getStringRepresentation(board);
        output.outputString(boardAsString);
    }
}
