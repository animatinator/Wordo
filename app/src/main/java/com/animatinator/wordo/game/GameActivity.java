package com.animatinator.wordo.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.animatinator.wordo.R;
import com.animatinator.wordo.crossword.PuzzleConfiguration;
import com.animatinator.wordo.crossword.PuzzleGenerationSettings;
import com.animatinator.wordo.crossword.PuzzleGenerator;
import com.animatinator.wordo.dictionaries.DictionaryLoader;

import java.io.IOException;

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
        } catch (IOException e) {
            Log.e(TAG, "Couldn't generate the puzzle!");
            e.printStackTrace();
        }
    }

    private PuzzleConfiguration generatePuzzle() throws IOException {
        DictionaryLoader dictionaryLoader = new DictionaryLoader(this);
        PuzzleGenerator generator =
                new PuzzleGenerator(dictionaryLoader.loadDictionary("english5000.txt"));
        return generator.createPuzzle(getGenerationSettings());
    }

    private PuzzleGenerationSettings getGenerationSettings() {
        return new PuzzleGenerationSettings()
                .withMaxWords(10)
                .withMinWordLength(3)
                .withNumLetters(7);
    }
}
