package com.animatinator.wordo.game.bonuswords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;

import com.animatinator.wordo.R;

import java.util.Collections;
import java.util.List;

/**
 * Displays the list of bonus words shown so far.
 */
public class BonusWordsDialogFragment extends DialogFragment {

    public static final String BONUS_WORDS_BUNDLE_ENTRY = "bonus_words";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.bonus_words);

        List<String> bonusWords = getArguments().getStringArrayList(BONUS_WORDS_BUNDLE_ENTRY);

        if (bonusWords == null || bonusWords.isEmpty()) {
            builder.setMessage("None so far!");
        } else {
            Collections.sort(bonusWords);
            builder.setMessage(TextUtils.join("\n", bonusWords));
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
