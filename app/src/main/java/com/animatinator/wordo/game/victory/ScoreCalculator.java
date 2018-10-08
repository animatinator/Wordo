package com.animatinator.wordo.game.victory;

import com.animatinator.wordo.game.stats.GameStatsMonitor;

/**
 * Computes scores based on {@link com.animatinator.wordo.game.stats.GameStatsMonitor.GameStats} at
 * the end of a game.
 *
 * The computed score will be an integer in the range [0, 10]. This can be divided by two to get a
 * standard star rating with halves.
 */
public class ScoreCalculator {
    public static int computeScore(GameStatsMonitor.GameStats gameStats) {
        float cost = getCost(gameStats);
        // The cost is divided by the number of words. We also ceiling it - a perfect five should
        // only happen in a perfect game.
        int scoreSubtraction = (int)Math.ceil((double)(cost / (float)gameStats.getNumWords()));

        return Math.max(0, 10 - scoreSubtraction);
    }

    private static float getCost(GameStatsMonitor.GameStats gameStats) {
        // Each hint has a cost of 4. Wrong words cost 2.
        float hintsAndWrongWordsCost =
                ((float) gameStats.getNumHints() * 4.0f) + ((float) gameStats.getNumWrongWords() * 2.0f);

        // Each bonus word counteracts 4 points of cost (one hint, two wrong words).
        float benefitOfBonusWords = 4.0f * gameStats.getNumBonusWords();

        return Math.max(0.0f, hintsAndWrongWordsCost - benefitOfBonusWords);
    }
}
