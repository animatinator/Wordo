package com.animatinator.wordo.game;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;

import com.animatinator.wordo.R;
import com.animatinator.wordo.crossword.PuzzleConfiguration;
import com.animatinator.wordo.crossword.PuzzleGenerationSettings;
import com.animatinator.wordo.crossword.PuzzleGenerator;
import com.animatinator.wordo.dictionaries.DictionaryLoader;
import com.animatinator.wordo.game.bonuswords.BonusWordsDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity {

    private static final String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // TODO: This is going to very slowly generate a puzzle on the main thread - stop this
        // madness as soon as possible.
        try {
            PuzzleConfiguration puzzleConfig = generatePuzzle();

            GameView gameView = findViewById(R.id.game_view);
            gameView.setLetters(puzzleConfig.getLetters());
            gameView.setPuzzleLayout(puzzleConfig.getLayout());
            gameView.setBonusWordsButtonCallback(bonusWords -> {
                DialogFragment fragment = new BonusWordsDialogFragment();
                fragment.setArguments(buildBundleFromBonusWords(bonusWords));
                fragment.show(getFragmentManager(), "Bonus words dialog");
            });
        } catch (IOException e) {
            Log.e(TAG, "Couldn't generate the puzzle!");
            e.printStackTrace();
        }
    }

    private Bundle buildBundleFromBonusWords(List<String> bonusWords) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(
                BonusWordsDialogFragment.BONUS_WORDS_BUNDLE_ENTRY, new ArrayList<>(bonusWords));
        return bundle;
    }

    private PuzzleConfiguration generatePuzzle() throws IOException {
        DictionaryLoader dictionaryLoader = new DictionaryLoader(this);
        PuzzleGenerator generator =
                new PuzzleGenerator(dictionaryLoader.loadDictionary("english"));
        return generator.createPuzzle(getGenerationSettings());
    }

    private PuzzleGenerationSettings getGenerationSettings() {
        return new PuzzleGenerationSettings()
                .withMaxWords(15)
                .withMinWordLength(3)
                .withNumLetters(7);
    }
}
