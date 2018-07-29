package com.animatinator.wordo.crossword.print;

import com.animatinator.wordo.crossword.board.Board;

/**
 * Combines human-readable with JSON output for easier debugging.
 */
public class BoardToDebugOutput implements BoardToString {
    private final BoardToHumanReadableString humanReadable = new BoardToHumanReadableString();
    private final BoardToJson json;

    public BoardToDebugOutput(boolean escapeQuotes) {
        json = new BoardToJson(escapeQuotes);
    }

    @Override
    public String getStringRepresentation(Board board) {
        return "Board:\n" +
                humanReadable.getStringRepresentation(board) +
                "\nJSON:\n" +
                json.getStringRepresentation(board);
    }
}
