package org.lushplugins.regrowthsmp.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@SuppressWarnings("unused")
@Command("regrowthsmp")
public class RegrowthSMPCommand {

    @Subcommand("reload")
    @CommandPermission("regrowthsmp.reload")
    public void reload(BukkitCommandActor actor) {
        RegrowthSMP.getInstance().getMainConfig().reload();

        actor.sender().sendMessage(Component.text()
            .content("RegrowthSMP has been reloaded")
            .color(TextColor.fromHexString("#b7faa2"))
            .build());
    }
}
