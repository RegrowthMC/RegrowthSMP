package org.lushplugins.regrowthsmp.command;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.lushplugins.lushlib.utils.timer.BossBarTimer;
import org.lushplugins.lushlib.utils.timer.RainbowBossBarTimer;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.command.suggest.TimerColor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

// TODO: Test
@SuppressWarnings("unused")
public class TimerCommand {

    @Command("timer")
    @CommandPermission("regrowthsmp.timer")
    public void timer(int duration, @TimerColor String rawColor, String label, @Optional @Flag("on-finish") String onFinish) {
        BossBarTimer timer;
        if (rawColor.equalsIgnoreCase("rainbow")) {
            timer = new RainbowBossBarTimer(label, BossBar.Overlay.NOTCHED_10, RegrowthSMP.getInstance(), duration);
        } else {
            BossBar.Color color = BossBar.Color.valueOf(rawColor.toUpperCase());
            timer = new BossBarTimer(label, color, BossBar.Overlay.NOTCHED_10, RegrowthSMP.getInstance(), duration);
        }

        timer.addOnlinePlayers();
        timer.start();

        if (onFinish != null) {
            timer.onFinish(() -> {
                ConsoleCommandSender console = Bukkit.getConsoleSender();
                Bukkit.dispatchCommand(console, onFinish);
            });
        }
    }
}
