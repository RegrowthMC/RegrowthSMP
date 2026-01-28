package org.lushplugins.regrowthsmp.module.pinata.command;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.lushlib.timer.BossBarTimer;
import org.lushplugins.lushlib.timer.RainbowBossBarTimer;
import org.lushplugins.lushlib.utils.TimeFormatter;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;

import java.awt.*;
import java.time.Duration;

public class SpawnPinataCommand extends Command {

    public SpawnPinataCommand() {
        super("spawnpinata");
        addRequiredPermission("%s.pinata.spawn".formatted(Pinata.getInstance().getPlugin().getName().toLowerCase()));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings, @NotNull String[] strings1) {
        int duration = Pinata.getInstance().getConfigManager().getSpawnDuration();
        BossBarTimer timer = new RainbowBossBarTimer(
            "<gradient:#FF8C8D:#FEB070:#FDE689:#DFFF94:#B2E6FC:#F0BDFF>Pinata spawning in %remaining_duration%!",
            BarStyle.SEGMENTED_10,
            Pinata.getInstance().getPlugin(),
            duration);
        timer.onFinish(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Pinata.getInstance().getConfigManager().getSpawnCommand()));
        timer.addOnlinePlayers();
        timer.start();
        Pinata.getInstance().getConfigManager().getOptionalMessage("pre-spawn")
            .ifPresent(message -> ChatColorHandler.broadcastMessage(message
                    .replace("%duration%", TimeFormatter.formatDuration(
                        Duration.ofSeconds(duration),
                        TimeFormatter.FormatType.LONG_FORM))));

        Pinata.getInstance().getConfigManager().getOptionalMessage("pre-spawn-discord")
            .ifPresent(message -> {
                String webhookUrl = Pinata.getInstance().getConfigManager().getDiscordWebhook();
                if (webhookUrl == null) {
                    return;
                }

                Bukkit.getScheduler().runTaskAsynchronously(Pinata.getInstance().getPlugin(), () -> {
                    try (WebhookClient client = WebhookClient.withUrl(webhookUrl)) {
                        client.send(new WebhookMessageBuilder()
                            .addEmbeds(new WebhookEmbedBuilder()
                                .setDescription(message
                                    .replace("%duration%", TimeFormatter.formatDuration(
                                        Duration.ofSeconds(duration),
                                        TimeFormatter.FormatType.LONG_FORM)))
                                .setColor(new Color(189, 255, 203).getRGB())
                                .build())
                            .build());
                    }
                });
            });
        return true;
    }
}
