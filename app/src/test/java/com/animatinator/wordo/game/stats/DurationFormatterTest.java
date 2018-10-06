package com.animatinator.wordo.game.stats;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class DurationFormatterTest {
    private static final long MS_IN_SECOND = 1000;
    private static final long SECONDS_IN_HOUR = 3600;
    private static final long SECONDS_IN_MINUTE = 60;

    private static final long ZERO_SECONDS = 0;
    private static final long ONE_SECOND = MS_IN_SECOND;
    private static final long TWO_SECONDS = MS_IN_SECOND * 2;
    private static final long ONE_MINUTE = ONE_SECOND * SECONDS_IN_MINUTE;
    private static final long ONE_MINUTE_SEVEN_SECONDS = ONE_MINUTE + (ONE_SECOND * 7);
    private static final long THREE_MINUTES_SEVEN_SECONDS = (ONE_MINUTE * 3) + (ONE_SECOND * 7);
    private static final long ONE_HOUR = ONE_SECOND * SECONDS_IN_HOUR;
    private static final long ONE_HOUR_THREE_MINUTES = ONE_HOUR + (3 * ONE_MINUTE);
    private static final long ONE_HOUR_ONE_MINUTE_THREE_SECONDS = ONE_HOUR + ONE_MINUTE + (3 * ONE_SECOND);
    private static final long TWO_HOURS_TWO_SECONDS = (2 * ONE_HOUR) + TWO_SECONDS;

    @Test
    public void zeroTime() {
        assertMapsTo(ZERO_SECONDS, "No time at all!");
    }

    @Test
    public void testSeconds() {
        assertMapsTo(ONE_SECOND, "1 second");
        assertMapsTo(TWO_SECONDS, "2 seconds");
    }

    @Test
    public void testMinutes() {
        assertMapsTo(ONE_MINUTE, "1 minute");
        assertMapsTo(ONE_MINUTE_SEVEN_SECONDS, "1 minute 7 seconds");
        assertMapsTo(THREE_MINUTES_SEVEN_SECONDS, "3 minutes 7 seconds");
    }

    @Test
    public void testHours() {
        assertMapsTo(ONE_HOUR, "1 hour");
        assertMapsTo(ONE_HOUR_THREE_MINUTES, "1 hour 3 minutes");
        assertMapsTo(ONE_HOUR_ONE_MINUTE_THREE_SECONDS, "1 hour 1 minute 3 seconds");
        assertMapsTo(TWO_HOURS_TWO_SECONDS, "2 hours 2 seconds");
    }

    private void assertMapsTo(long ms, String representation) {
        DurationFormatter formatter = new DurationFormatter(ms);
        assertEquals(representation, formatter.getAsString());
    }
}