package com.animatinator.wordo.game.victory;

import com.animatinator.wordo.game.stats.GameStatsMonitor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoreCalculatorTest {
    @Test
    public void perfectGame() {
        GameStatsMonitor monitor = new GameStatsMonitor(10);
        assertEquals(10, ScoreCalculator.computeScore(monitor.getGameStatsNow()));
    }

    @Test
    public void godDamnedAtrociousPerformance() {
        GameStatsMonitor monitor = new GameStatsMonitor(1);

        requestHints(monitor, 10);

        // Shouldn't go below zero!
        assertEquals(0, ScoreCalculator.computeScore(monitor.getGameStatsNow()));
    }

    @Test
    public void someHints() {
        GameStatsMonitor monitor = new GameStatsMonitor(10);

        requestHints(monitor, 5);

        assertEquals(8, ScoreCalculator.computeScore(monitor.getGameStatsNow()));
    }

    @Test
    public void manyManyWrongGuesses() {
        GameStatsMonitor monitor = new GameStatsMonitor(10);

        makeWrongGuesses(monitor, 20);

        assertEquals(6, ScoreCalculator.computeScore(monitor.getGameStatsNow()));
    }

    @Test
    public void badPerformanceButSoManyBonusWordsWow() {
        GameStatsMonitor monitor = new GameStatsMonitor(10);

        makeWrongGuesses(monitor, 10);
        requestHints(monitor, 5);
        monitor.setNumBonusWords(5);

        assertEquals(8, ScoreCalculator.computeScore(monitor.getGameStatsNow()));
    }

    /**
     * Make sure rounding doesn't let us get away with five stars for an imperfect game.
     */
    @Test
    public void imperfectGameIsImperfect() {
        GameStatsMonitor monitor = new GameStatsMonitor(100);

        monitor.hintRequested();

        assertEquals(9, ScoreCalculator.computeScore(monitor.getGameStatsNow()));
    }

    private void requestHints(GameStatsMonitor monitor, int numHints) {
        for (int i = 0; i < numHints; i++) {
            monitor.hintRequested();
        }
    }

    private void makeWrongGuesses(GameStatsMonitor monitor, int numWrongGuesses) {
        for (int i = 0; i < numWrongGuesses; i++) {
            monitor.wrongWordEntered();
        }
    }
}