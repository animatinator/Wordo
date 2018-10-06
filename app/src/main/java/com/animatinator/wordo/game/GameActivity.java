package com.animatinator.wordo.game;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.animatinator.wordo.IntentConstants;
import com.animatinator.wordo.R;
import com.animatinator.wordo.crossword.PuzzleConfiguration;
import com.animatinator.wordo.game.bonuswords.BonusWordsDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity {

    private static final String TAG = "GameActivity";
    private static final long NO_PUZZLE_ID = -1;

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PuzzleConfiguration puzzleConfig = loadPuzzleConfiguration();
        if (puzzleConfig == null) return;

        setPuzzleInGameView(puzzleConfig);
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

    private void setPuzzleInGameView(PuzzleConfiguration puzzleConfiguration) {
        gameView = findViewById(R.id.game_view);
        gameView.setLetters(puzzleConfiguration.getLetters());
        gameView.setPuzzleLayout(puzzleConfiguration.getLayout());
        gameView.setBonusWordsButtonCallback(bonusWords -> {
            DialogFragment fragment = new BonusWordsDialogFragment();
            fragment.setArguments(buildBundleFromBonusWords(bonusWords));
            fragment.show(getFragmentManager(), "Bonus words dialog");
        });
        gameView.setVictoryCallback(() -> Log.e(TAG, "Victory!"));
    }

    private Bundle buildBundleFromBonusWords(List<String> bonusWords) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(
                BonusWordsDialogFragment.BONUS_WORDS_BUNDLE_ENTRY, new ArrayList<>(bonusWords));
        return bundle;
    }

}
