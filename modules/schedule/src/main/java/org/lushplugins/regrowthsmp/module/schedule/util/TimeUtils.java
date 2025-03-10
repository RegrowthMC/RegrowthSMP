package org.lushplugins.regrowthsmp.module.schedule.util;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)(\\w+)");

    public static long getDelayInSeconds(String delayRaw) throws IllegalArgumentException {
        Matcher matcher = TIME_PATTERN.matcher(delayRaw);
        if (matcher.find()) {
            long amount = Long.parseLong(matcher.group(1));
            TimeUnit unit = switch (matcher.group(2)) {
                case "s", "secs", "seconds" -> TimeUnit.SECONDS;
                case "m", "mins", "minutes" -> TimeUnit.MINUTES;
                case "h", "hours" -> TimeUnit.HOURS;
                case "d", "days" -> TimeUnit.DAYS;
                default -> throw new IllegalArgumentException("Invalid time format");
            };

            return unit.toSeconds(amount);
        } else {
            throw new IllegalArgumentException("Invalid time format");
        }
    }
}
