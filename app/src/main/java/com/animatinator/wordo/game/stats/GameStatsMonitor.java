package com.animatinator.wordo.game.stats;

import java.util.Calendar;
import java.util.Date;

public class GameStatsMonitor {
    private Date startTime;
    private int numHints = 0;

    public GameStatsMonitor() {
        startTime = Calendar.getInstance().getTime();
    }

    public void hintRequested() {
        numHints++;
    }

    public GameStats getGameStatsNow() {
        Date endTime = Calendar.getInstance().getTime();
        return new GameStats(startTime, endTime, numHints);
    }

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
