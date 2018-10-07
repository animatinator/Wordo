package com.animatinator.wordo.game.stats;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Records statistics about a game. Because these are time-based, data can only be accessed in
 * snapshot form by calling {@link GameStatsMonitor#getGameStatsNow()}.
 */
public class GameStatsMonitor {
    private Date startTime;
    private int numHints = 0;
    private int numWrongWords = 0;
    private int numBonusWords;

    public GameStatsMonitor() {
        startTime = Calendar.getInstance().getTime();
    }

    /**
     * Notify that the player used a hint.
     */
    public void hintRequested() {
        numHints++;
    }

    public void wrongWordEntered() {
        numWrongWords++;
    }

    /**
     * Update the number of bonus words found.
     */
    public void setNumBonusWords(int numBonusWords) {
        this.numBonusWords = numBonusWords;
    }

    /**
     * Take a snapshot of the stats.
     */
    public GameStats getGameStatsNow() {
        Date endTime = Calendar.getInstance().getTime();
        return new GameStats(startTime, endTime, numHints, numWrongWords, numBonusWords);
    }

    /**
     * A snapshot of the game's statistics at a given time.
     */
    public static final class GameStats implements Serializable {
        private final Date startTime;
        private final Date endTime;
        private final int numHints;
        private final int numWrongWords;
        private final int numBonusWords;

        GameStats(Date startTime, Date endTime, int numHints, int numWrongWords, int numBonusWords) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.numHints = numHints;
            this.numWrongWords = numWrongWords;
            this.numBonusWords = numBonusWords;
        }

        public long getDuration() {
            return endTime.getTime() - startTime.getTime();
        }

        public int getNumHints() {
            return numHints;
        }

        public int getNumWrongWords() {
            return numWrongWords;
        }

        public int getNumBonusWords() {
            return numBonusWords;
        }
    }
}
