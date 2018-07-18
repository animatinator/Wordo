package com.animatinator.wordo.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.animatinator.wordo.R;
import com.animatinator.wordo.game.keyboard.RotaryKeyboard;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GameView gameView = findViewById(R.id.game_view);
        gameView.setLetters(new String[] {"C", "A", "U", "S", "E", "D"});
    }
}
