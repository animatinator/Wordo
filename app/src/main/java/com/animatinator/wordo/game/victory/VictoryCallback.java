package com.animatinator.wordo.game.victory;

import com.animatinator.wordo.game.stats.GameStatsMonitor;

public interface VictoryCallback {
    void onVictory(GameStatsMonitor.GameStats gameStats);
}
