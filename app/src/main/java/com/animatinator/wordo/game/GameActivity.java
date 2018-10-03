package com.animatinator.wordo.game;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;

import com.animatinator.wordo.R;
import com.animatinator.wordo.crossword.PuzzleConfiguration;
import com.animatinator.wordo.crossword.PuzzleGenerationProgressCallback;
import com.animatinator.wordo.crossword.PuzzleGenerationSettings;
import com.animatinator.wordo.crossword.PuzzleGenerator;
import com.animatinator.wordo.dictionaries.DictionaryLoader;
import com.animatinator.wordo.game.bonuswords.BonusWordsDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameActivity extends Activity {

    private static final String TAG = "GameActivity";

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        synchronized (this) {
            gameView = findViewById(R.id.game_view);
            gameView.setBonusWordsButtonCallback(bonusWords -> {
                DialogFragment fragment = new BonusWordsDialogFragment();
                fragment.setArguments(buildBundleFromBonusWords(bonusWords));
                fragment.show(getFragmentManager(), "Bonus words dialog");
            });
        }

        generatePuzzleOnBackgroundThread();
    }

    private void generatePuzzleOnBackgroundThread() {
        Executor configExecutor = Executors.newSingleThreadExecutor();

        configExecutor.execute(() -> {
            try {
                PuzzleConfiguration puzzleConfig =
                        generatePuzzle(new DebugLoggingPuzzleGenerationCallback());
                synchronized (this) {
                    gameView.setLetters(puzzleConfig.getLetters());
                    gameView.setPuzzleLayout(puzzleConfig.getLayout());
                }
            } catch (IOException e) {
                Log.e(TAG, "Couldn't generate the puzzle");
                e.printStackTrace();
            }
        });
    }

    private Bundle buildBundleFromBonusWords(List<String> bonusWords) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(
                BonusWordsDialogFragment.BONUS_WORDS_BUNDLE_ENTRY, new ArrayList<>(bonusWords));
        return bundle;
    }

    private PuzzleConfiguration generatePuzzle(
            PuzzleGenerationProgressCallback progressCallback) throws IOException {
        DictionaryLoader dictionaryLoader = new DictionaryLoader(this);
        PuzzleGenerator generator =
                new PuzzleGenerator(dictionaryLoader.loadDictionary("english"));
        return generator.createPuzzle(getGenerationSettings(), progressCallback);
    }

    private PuzzleGenerationSettings getGenerationSettings() {
        return new PuzzleGenerationSettings()
                .withMaxWords(15)
                .withMinWordLength(3)
                .withNumLetters(7);
    }

    private static final class DebugLoggingPuzzleGenerationCallback
            implements PuzzleGenerationProgressCallback {
        @Override
        public void setGenerationState(String state) {
            Log.i(TAG, "Puzzle generation state: "+state);
        }

        @Override
        public void setProgress(double progress) {
            Log.i(TAG, "Puzzle generation progress set to: "+progress);
        }
    }
}
