package org.tudublin.bonsaiapp.util;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.tudublin.bonsaiapp.R;

public final class DifficultyUtils {

    private DifficultyUtils() {}

    public static int chipBackground(String level) {
        String key = level == null ? "" : level.trim().toLowerCase();
        switch (key) {
            case "expert":
            case "hard":
                return R.drawable.bg_chip_hard;
            case "intermediate":
            case "medium":
                return R.drawable.bg_chip_medium;
            default:
                return R.drawable.bg_chip_easy;
        }
    }

    public static int chipTextColor(String level) {
        String key = level == null ? "" : level.trim().toLowerCase();
        switch (key) {
            case "expert":
            case "hard":
                return R.color.chip_hard_fg;
            case "intermediate":
            case "medium":
                return R.color.chip_medium_fg;
            default:
                return R.color.chip_easy_fg;
        }
    }

    public static void applyTo(TextView view, String level) {
        Context ctx = view.getContext();
        view.setBackgroundResource(chipBackground(level));
        view.setTextColor(ContextCompat.getColor(ctx, chipTextColor(level)));
        view.setText(level);
    }
}