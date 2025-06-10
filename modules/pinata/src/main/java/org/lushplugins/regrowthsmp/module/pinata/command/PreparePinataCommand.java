package org.lushplugins.regrowthsmp.module.pinata.command;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;

import java.awt.*;

public class PreparePinataCommand extends Command {

    public PreparePinataCommand() {
        super("preparepinata");
        addRequiredPermission("%s.pinata.spawn".formatted(Pinata.getInstance().getPlugin().getName().toLowerCase()));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings, @NotNull String[] strings1) {
        Pinata.getInstance().getConfigManager().getOptionalMessage("prepare-pinata-discord")
            .ifPresent(message -> {
                String webhookUrl = Pinata.getInstance().getConfigManager().getDiscordWebhook();
                if (webhookUrl == null) {
                    return;
                }

                Bukkit.getScheduler().runTaskAsynchronously(Pinata.getInstance().getPlugin(), () -> {
                    try (WebhookClient client = WebhookClient.withUrl(webhookUrl)) {
                        client.send(new WebhookMessageBuilder()
                            .addEmbeds(new WebhookEmbedBuilder()
                                .setDescription(message)
                                .setColor(new Color(189, 255, 203).getRGB())
                                .build())
                            .build());
                    }
                });
            });
        return true;
    }
}
