package com.animatinator.wordo.game.stats;

import java.util.Locale;

public class DurationFormatter {
    private static final long MS_IN_SECOND = 1000;
    private static final long SECONDS_IN_HOUR = 3600;
    private static final long SECONDS_IN_MINUTE = 60;

    private static final String NO_TIME_STRING = "No time at all!";

    private final long duration;

    public DurationFormatter(long duration) {
        this.duration = duration;
    }

    public String getAsString() {
        if (duration == 0) {
            return NO_TIME_STRING;
        }

        StringBuilder timeStringBuilder = new StringBuilder();

        long rawSeconds = duration / MS_IN_SECOND;
        long hours = rawSeconds / SECONDS_IN_HOUR;

        if (hours == 1) {
            timeStringBuilder.append("1 hour ");
        } else if (hours > 1) {
            timeStringBuilder.append(String.format(Locale.UK, "%d hours ", hours));
        }

        rawSeconds = rawSeconds % SECONDS_IN_HOUR;

        long minutes = rawSeconds / SECONDS_IN_MINUTE;

        if (minutes == 1) {
            timeStringBuilder.append("1 minute ");
        } else if (minutes > 1) {
            timeStringBuilder.append(String.format(Locale.UK, "%d minutes ", minutes));
        }

        rawSeconds = rawSeconds % SECONDS_IN_MINUTE;

        if (rawSeconds == 1) {
            timeStringBuilder.append("1 second");
        } else if (rawSeconds > 1) {
            timeStringBuilder.append(String.format(Locale.UK, "%d seconds", rawSeconds));
        }

        return timeStringBuilder.toString().trim();
    }
}
