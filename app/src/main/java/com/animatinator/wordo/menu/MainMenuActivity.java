package com.animatinator.wordo.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.animatinator.wordo.R;
import com.animatinator.wordo.config.ConfigNames;
import com.animatinator.wordo.config.Difficulty;
import com.animatinator.wordo.config.PuzzleSize;
import com.animatinator.wordo.game.util.Typefaces;
import com.animatinator.wordo.loadingscreen.LoadingActivity;

public class MainMenuActivity extends Activity {

    private Difficulty difficulty = Difficulty.NORMAL;
    private PuzzleSize puzzleSize = PuzzleSize.MEDIUM;

    private Button difficultyButton;
    private Button sizeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        TextView title = findViewById(R.id.main_menu_title);
        title.setTypeface(Typefaces.getBoldTypeface(this));
        difficultyButton = findViewById(R.id.difficulty_button);
        sizeButton = findViewById(R.id.size_button);
        updateButtonText();

    }

    public void launchGameActivity(View view) {
        Intent intent =
                new Intent(
                        this, LoadingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void handleConfigToggle(View view) {
        switch (view.getId()) {
            case R.id.difficulty_button:
                difficulty = difficulty.next();
                break;
            case R.id.size_button:
                puzzleSize = puzzleSize.next();
                break;
            default:
                break;
        }

        updateButtonText();
    }

    private void updateButtonText() {
        difficultyButton.setText(ConfigNames.getStringForDifficulty(this, difficulty));
        sizeButton.setText(ConfigNames.getStringForPuzzleSize(this, puzzleSize));
    }
}
