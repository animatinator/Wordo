package com.animatinator.wordo.loadingscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.animatinator.wordo.IntentConstants;
import com.animatinator.wordo.R;
import com.animatinator.wordo.crossword.PuzzleConfiguration;
import com.animatinator.wordo.crossword.PuzzleGenerationProgressCallback;
import com.animatinator.wordo.crossword.PuzzleGenerationSettings;
import com.animatinator.wordo.crossword.PuzzleGenerator;
import com.animatinator.wordo.dictionaries.DictionaryLoader;
import com.animatinator.wordo.game.GameActivity;
import com.animatinator.wordo.game.PuzzleStore;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadingActivity extends Activity {

    private static final String TAG = "LoadingActivity";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.progressBar);

        generatePuzzleOnBackgroundThread();
    }

    private void generatePuzzleOnBackgroundThread() {
        Executor configExecutor = Executors.newSingleThreadExecutor();

        configExecutor.execute(() -> {
            try {
                PuzzleConfiguration puzzleConfig =
                        generatePuzzle(new GenerationProgressListener());
                long id = PuzzleStore.storePuzzleConfiguration(puzzleConfig);
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(IntentConstants.PUZZLE_CONFIGURATION_ID, id);
                startActivity(intent);
            } catch (IOException e) {
                Log.e(TAG, "Couldn't generate the puzzle");
                e.printStackTrace();
            }
        });
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

    private final class GenerationProgressListener implements PuzzleGenerationProgressCallback {
        @Override
        public void setGenerationState(String state) {
            Log.i(TAG, "Puzzle generation state: "+state);
        }

        @Override
        public void setProgress(double progress) {
            Log.i(TAG, "Puzzle generation progress set to: "+progress);
            progressBar.setProgress((int)(progress * 100.0d));
        }
    }
}
