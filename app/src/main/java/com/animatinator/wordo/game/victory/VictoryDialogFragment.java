package com.animatinator.wordo.game.victory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.animatinator.wordo.R;
import com.animatinator.wordo.game.stats.DurationFormatter;
import com.animatinator.wordo.game.stats.GameStatsMonitor;

public class VictoryDialogFragment extends DialogFragment {

    public static final String GAME_STATS_BUNDLE_ENTRY = "game_stats";

    public interface Callback {
        void onChoosePlayAgain();
        void onChooseExit();
    }

    private VictoryDialogCallbackWrapper callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.victory_title);

        GameStatsMonitor.GameStats gameStats =
                (GameStatsMonitor.GameStats) getArguments().getSerializable(GAME_STATS_BUNDLE_ENTRY);

        builder.setMessage(buildMessageFromGameState(gameStats));

        builder.setPositiveButton(
                R.string.victory_play_again_button,
                (dialogInterface, i) -> callback.onChoosePlayAgain());
        builder.setNegativeButton(
                R.string.victory_exit_button, (dialogInterface, i) -> callback.onChooseExit());

        return builder.create();
    }

    private String buildMessageFromGameState(GameStatsMonitor.GameStats gameStats) {
        if (gameStats == null) {
            return getString(R.string.victory_message_no_stats);
        } else {
            String formatString = getString(R.string.victory_message);
            return String.format(
                    formatString,
                    getDurationAsString(gameStats.getDuration()),
                    gameStats.getNumHints(),
                    gameStats.getNumBonusWords());
        }
    }

    private String getDurationAsString(long duration) {
        return new DurationFormatter(duration).getAsString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            // Instantiate the Callback so we can send events to the host
            callback = new VictoryDialogCallbackWrapper((Callback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement VictoryDialogFragment.Callback");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        callback.onDismiss();
    }

}