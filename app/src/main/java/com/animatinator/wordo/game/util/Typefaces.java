package com.animatinator.wordo.game.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.animatinator.wordo.R;

public class Typefaces {
    public static Typeface getBoldTypeface(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            return getTypeface(context, R.font.glacial_indifference_bold);
        } else {
            return getTypeface(context, "glacial_indifference_bold.otf");
        }
    }

    public static Typeface getNormalTypeface(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            return getTypeface(context, R.font.glacial_indifference_regular);
        } else {
            return getTypeface(context, "glacial_indifference_regular.otf");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Typeface getTypeface(Context context, int id) {
        return context.getResources().getFont(id);
    }

    private static Typeface getTypeface(Context context, String name) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/"+name);
    }
}
