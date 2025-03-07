package org.lushplugins.regrowthsmp.module.schedule.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.regrowthsmp.module.schedule.Schedule;
import org.lushplugins.regrowthsmp.module.schedule.schedule.ScheduleManager;
import org.lushplugins.regrowthsmp.module.schedule.schedule.action.ScheduledCommands;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Command Format: /schedule <delay> <command>
public class ScheduleCommand extends Command {
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)(\\w+)");

    public ScheduleCommand() {
        super("schedule");
        addRequiredPermission("%s.utilities.schedule".formatted(Schedule.getInstance().getPlugin().getName().toLowerCase()));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        if (args.length < 2) {
            // TODO: Message
            return true;
        }

        long delay;
        try {
            delay = getDelayInSeconds(args[0]);
        } catch (IllegalArgumentException e) {
            // TODO: Message
            return true;
        }

        long triggerEpoch = Instant.now().getEpochSecond() + delay;
        ScheduledCommands action = new ScheduledCommands(List.of(
            String.join(" ", Arrays.copyOfRange(args, 1, args.length)).split("&& ")));

        ScheduleManager scheduleManager = Schedule.getInstance().getScheduleManager();
        scheduleManager.scheduleActionAt(triggerEpoch, action);
        scheduleManager.updateTask();
        // TODO: Message
        return true;
    }

    private static long getDelayInSeconds(String delayRaw) throws IllegalArgumentException {
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
