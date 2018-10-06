package com.animatinator.wordo.game.stats;

import java.util.Calendar;
import java.util.Date;

/**
 * Records statistics about a game. Because these are time-based, data can only be accessed in
 * snapshot form by calling {@link GameStatsMonitor#getGameStatsNow()}.
 */
public class GameStatsMonitor {
    private Date startTime;
    private int numHints = 0;

    public GameStatsMonitor() {
        startTime = Calendar.getInstance().getTime();
    }

    /**
     * Notify that the player used a hint.
     */
    public void hintRequested() {
        numHints++;
    }

    /**
     * Take a snapshot of the stats.
     */
    public GameStats getGameStatsNow() {
        Date endTime = Calendar.getInstance().getTime();
        return new GameStats(startTime, endTime, numHints);
    }

    /**
     * A snapshot of the game's statistics at a given time.
     */
    public static final class GameStats {
        private final Date startTime;
        private final Date endTime;
        private final int numHints;

        GameStats(Date startTime, Date endTime, int numHints) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.numHints = numHints;
        }

        public long getDuration() {
            return endTime.getTime() - startTime.getTime();
        }

        public int getNumHints() {
            return numHints;
        }
    }
}
