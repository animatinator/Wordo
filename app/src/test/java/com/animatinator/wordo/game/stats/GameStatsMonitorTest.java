package com.animatinator.wordo.game.stats;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameStatsMonitorTest {

    private GameStatsMonitor gameStatsMonitor;

    @Before
    public void setUpGameStatsMonitor() {
        gameStatsMonitor = new GameStatsMonitor(100);
    }

    @Test
    public void timeElapsed() throws InterruptedException {
        // TODO: This is terrible. Unfortunately, we can't mock out GameStatsMonitor's sense of time
        // as-is. Consider fixing.
        Thread.sleep(10);
        GameStatsMonitor.GameStats gameStats = gameStatsMonitor.getGameStatsNow();
        assertTrue(gameStats.getDuration() > 0);
    }

    @Test
    public void countHints() {
        gameStatsMonitor.hintRequested();
        gameStatsMonitor.hintRequested();
        gameStatsMonitor.hintRequested();

        GameStatsMonitor.GameStats stats = gameStatsMonitor.getGameStatsNow();

        assertEquals(3, stats.getNumHints());
    }

    @Test
    public void countWrongWords() {
        gameStatsMonitor.wrongWordEntered();
        gameStatsMonitor.wrongWordEntered();
        gameStatsMonitor.wrongWordEntered();

        GameStatsMonitor.GameStats stats = gameStatsMonitor.getGameStatsNow();

        assertEquals(3, stats.getNumWrongWords());
    }

    /**
     * Vefify that snapshots aren't modified by future changes to the monitor.
     */
    @Test
    public void shapshot() {
        gameStatsMonitor.hintRequested();
        gameStatsMonitor.wrongWordEntered();
        gameStatsMonitor.setNumBonusWords(3);

        GameStatsMonitor.GameStats stats = gameStatsMonitor.getGameStatsNow();
        gameStatsMonitor.hintRequested();
        gameStatsMonitor.wrongWordEntered();
        gameStatsMonitor.setNumBonusWords(5);

        assertEquals(1, stats.getNumHints());
        assertEquals(1, stats.getNumWrongWords());
        assertEquals(3, stats.getNumBonusWords());
    }
}