package org.lushplugins.regrowthsmp.module.utilities.command;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.lushlib.timer.BossBarTimer;
import org.lushplugins.lushlib.timer.RainbowBossBarTimer;
import org.lushplugins.regrowthsmp.module.utilities.Utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Command Format: /timer <duration> <colour> <label>
public class TimerCommand extends Command {

    public TimerCommand() {
        super("timer");
        addRequiredPermission("%s.utilities.timer".formatted(Utilities.getInstance().getPlugin().getName().toLowerCase()));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        if (args.length < 3) {
            // TODO: Message
            return true;
        }

        int duration = Integer.parseInt(args[0]);
        String colorRaw = args[1];
        String timerLabel = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        BossBarTimer timer;
        if (colorRaw.equalsIgnoreCase("rainbow")) {
            timer = new RainbowBossBarTimer(timerLabel, BarStyle.SEGMENTED_10, Utilities.getInstance().getPlugin(), duration);
        } else {
            BarColor color;
            try {
                color = BarColor.valueOf(colorRaw.toUpperCase());
            } catch (IllegalArgumentException e) {
                // TODO: Message
                return true;
            }

            timer = new BossBarTimer(timerLabel, color, BarStyle.SEGMENTED_10, Utilities.getInstance().getPlugin(), duration);
        }

        timer.addOnlinePlayers();
        timer.start();
        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        return switch (fullArgs.length) {
            case 1 -> Collections.singletonList("<duration>");
            case 2 -> {
                List<String> results = Arrays.stream(BarColor.values())
                    .map(barColor -> barColor.name().toLowerCase())
                    .collect(Collectors.toList());
                results.add("rainbow");

                yield results;
            }
            default -> Collections.singletonList("<label>");
        };
    }
}
