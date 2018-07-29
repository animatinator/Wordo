package com.animatinator.wordo.crossword.print;

import com.animatinator.wordo.crossword.board.Board;
import com.animatinator.wordo.crossword.board.BoardLayout;
import com.animatinator.wordo.crossword.util.BoardOffset;
import com.animatinator.wordo.crossword.util.BoardPosition;

import java.util.Optional;

public class BoardToJson implements BoardToString {
    private static final String QUOTE = "\"";
    private static final String QUOTE_ESCAPED = "\\\"";

    private final boolean escapeQuotes;

    BoardToJson(boolean escapeQuotes) {
        this.escapeQuotes = escapeQuotes;
    }

    BoardToJson() {
        this(false);
    }

    @Override
    public String getStringRepresentation(Board board) {
        BoardLayout layout = board.getLayout();
        StringBuilder builder = new StringBuilder("[");

        String quote = escapeQuotes ? QUOTE_ESCAPED : QUOTE;

        BoardPosition topLeft = layout.getTopLeft();

        for (int y = 0; y < layout.getHeight(); y++) {
            builder.append("[");
            for (int x = 0; x < layout.getWidth(); x++) {
                // TODO: This is a bit of a hack: we're offsetting the coordinates we pass in to account for the inverse
                // offset the layout does internally.
                Optional<String> tileStringOptional =
                        layout.getAt(new BoardPosition(x, y).withOffset(new BoardOffset(topLeft)));
                if (tileStringOptional.isPresent()) {
                    builder.append(String.format("%s%s%s", quote, tileStringOptional.get(), quote));
                } else {
                    builder.append("null");
                }
                if (x < layout.getWidth() - 1) {
                    builder.append(", ");
                }
            }
            builder.append("]");
            if (y < layout.getHeight() - 1) {
                builder.append(", ");
            }
        }

        builder.append("]");

        return builder.toString();
    }
}
