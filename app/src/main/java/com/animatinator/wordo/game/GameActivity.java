package com.animatinator.wordo.game;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.animatinator.wordo.IntentConstants;
import com.animatinator.wordo.R;
import com.animatinator.wordo.crossword.PuzzleConfiguration;
import com.animatinator.wordo.game.bonuswords.BonusWordsDialogFragment;
import com.animatinator.wordo.game.stats.GameStatsMonitor;
import com.animatinator.wordo.game.victory.VictoryDialogFragment;
import com.animatinator.wordo.loadingscreen.LoadingActivity;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity implements VictoryDialogFragment.Callback {

    private static final String TAG = "GameActivity";
    private static final long NO_PUZZLE_ID = -1;

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PuzzleConfiguration puzzleConfig = loadPuzzleConfiguration();
        if (puzzleConfig == null) return;

        gameView = findViewById(R.id.game_view);
        setPuzzleInGameView(gameView, puzzleConfig);
        setUpCallbacks(gameView);
    }

    @Nullable
    private PuzzleConfiguration loadPuzzleConfiguration() {
        long savedPuzzleId =
                getIntent().getExtras().getLong(
                        IntentConstants.PUZZLE_CONFIGURATION_ID, -1L);

        if (savedPuzzleId == NO_PUZZLE_ID) {
            Log.e(TAG, "No puzzle ID passed to GameActivity!");
            return null;
        }

        PuzzleConfiguration puzzleConfig = PuzzleStore.loadPuzzleConfiguration(savedPuzzleId);
        if (puzzleConfig == null) {
            Log.e(TAG, "Couldn't find a puzzle with ID "+savedPuzzleId+"!");
            return null;
        }

        return puzzleConfig;
    }

    private void setPuzzleInGameView(GameView gameView, PuzzleConfiguration puzzleConfiguration) {
        gameView.setLetters(puzzleConfiguration.getLetters());
        gameView.setPuzzleLayout(puzzleConfiguration.getLayout());
    }

    private void setUpCallbacks(GameView gameView) {
        gameView.setBonusWordsButtonCallback(this::showBonusWordsDialog);
        gameView.setVictoryCallback(this::showVictoryDialog);
    }

    private void showBonusWordsDialog(List<String> bonusWords) {
        DialogFragment fragment = new BonusWordsDialogFragment();
        fragment.setArguments(buildBundleFromBonusWords(bonusWords));
        fragment.show(getFragmentManager(), "Bonus words dialog");
    }

    private void showVictoryDialog(GameStatsMonitor.GameStats gameStats) {
        DialogFragment fragment = new VictoryDialogFragment();
        fragment.setArguments(buildGameStatsBundle(gameStats));
        fragment.show(getFragmentManager(), "VictoryDialog");
    }

    private Bundle buildBundleFromBonusWords(List<String> bonusWords) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(
                BonusWordsDialogFragment.BONUS_WORDS_BUNDLE_ENTRY, new ArrayList<>(bonusWords));
        return bundle;
    }

    private Bundle buildGameStatsBundle(GameStatsMonitor.GameStats stats) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VictoryDialogFragment.GAME_STATS_BUNDLE_ENTRY, stats);
        return bundle;
    }

    @Override
    public void onChoosePlayAgain() {
        Intent intent =
                new Intent(
                        this, LoadingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void onChooseExit() {
        finish();
    }
}
