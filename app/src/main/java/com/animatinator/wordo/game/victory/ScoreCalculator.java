package com.animatinator.wordo.game.victory;

import com.animatinator.wordo.game.stats.GameStatsMonitor;

/**
 * Computes scores based on {@link com.animatinator.wordo.game.stats.GameStatsMonitor.GameStats} at
 * the end of a game.
 */
public class ScoreCalculator {
    public static int computeScore(GameStatsMonitor.GameStats gameStats) {
        float cost = getCost(gameStats);
        // The cost is divided by the number of words. We also ceiling it - a perfect five should
        // only happen in a perfect game.
        int scoreSubtraction = (int)Math.ceil((double)(cost / (float)gameStats.getNumWords()));

        return Math.max(0, 5 - scoreSubtraction);
    }

    private static float getCost(GameStatsMonitor.GameStats gameStats) {
        // Each hint has a cost of 2. Wrong words cost 1.
        float hintsAndWrongWordsCost =
                ((float) gameStats.getNumHints() * 2.0f) + ((float) gameStats.getNumWrongWords());

        // Each bonus word counteracts two points of cost (two hints, six wrong words).
        float benefitOfBonusWords = 2.0f * gameStats.getNumBonusWords();

        return Math.max(0.0f, hintsAndWrongWordsCost - benefitOfBonusWords);
    }
}
