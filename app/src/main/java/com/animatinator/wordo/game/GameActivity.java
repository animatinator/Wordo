package com.animatinator.wordo.game;

import android.app.Activity;
import android.os.Bundle;

import com.animatinator.wordo.R;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
