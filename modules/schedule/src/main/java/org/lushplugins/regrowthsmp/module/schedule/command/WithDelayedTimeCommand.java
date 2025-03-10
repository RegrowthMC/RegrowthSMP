package org.lushplugins.regrowthsmp.module.schedule.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.regrowthsmp.module.schedule.Schedule;
import org.lushplugins.regrowthsmp.module.schedule.util.TimeUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class WithDelayedTimeCommand extends Command {

    public WithDelayedTimeCommand() {
        super("withdelayedtime");
        addRequiredPermission("%s.utilities.withdelayedtime".formatted(Schedule.getInstance().getPlugin().getName().toLowerCase()));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        if (args.length < 2) {
            // TODO: Message
            return true;
        }

        long delay;
        try {
            delay = TimeUtils.getDelayInSeconds(args[0]);
        } catch (IllegalArgumentException e) {
            // TODO: Message
            return true;
        }

        Instant delayedInstant = Instant.now().plusSeconds(delay);

        String[] commands = String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                .replace("%time%", delayedInstant.atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm")))
                .split("&& ");

        for (String aCommand : commands) {
            Bukkit.dispatchCommand(sender, aCommand);
        }

        // TODO: Message
        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        return switch (fullArgs.length) {
            case 0 -> null;
            case 1 -> List.of("<delay>");
            default -> List.of("<command>");
        };
    }
}