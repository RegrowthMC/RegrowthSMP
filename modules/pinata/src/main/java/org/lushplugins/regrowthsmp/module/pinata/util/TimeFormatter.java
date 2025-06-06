package org.lushplugins.regrowthsmp.module.pinata.util;

import java.time.Duration;

public class TimeFormatter {

    public static String formatDuration(long durationInSeconds) {
        Duration duration = Duration.ofSeconds(durationInSeconds);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append("h ");
        }

        if (minutes > 0) {
            result.append(minutes).append("m ");
        }

        if (seconds > 0) {
            result.append(seconds).append("s ");
        }

        return result.toString().trim();
    }
}
