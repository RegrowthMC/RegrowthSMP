package org.lushplugins.regrowthsmp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.command.SubCommand;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.module.ModuleType;

import java.util.List;

public class ReloadCommand extends SubCommand {

    public ReloadCommand() {
        super("reload");
        addRequiredPermission("regrowthsmp.reload");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        RegrowthSMP.getInstance().getConfigManager().reload();

        if (args.length != 0) {
            try {
                ModuleType moduleType = ModuleType.valueOf(args[0].toUpperCase());
                RegrowthSMP.getInstance().getModuleManager().getModule(moduleType).reload();
                ChatColorHandler.sendMessage(sender, String.format("&#b7faa2RegrowthSMP has successfully reloaded &#66b04f%s 🔃", moduleType.name().toLowerCase()));
            } catch (IllegalArgumentException e) {
                ChatColorHandler.sendMessage(sender, "&#ff6969RegrowthSMP has failed to reload");
            }
        } else {
            ChatColorHandler.sendMessage(sender, "&#b7faa2RegrowthSMP has been reloaded &#66b04f🔃");
        }

        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        if (args.length == 1) {
            return RegrowthSMP.getInstance().getModuleManager().getModuleTypes().stream()
                .map(moduleType -> moduleType.name().toLowerCase())
                .toList();
        } else {
            return null;
        }
    }
}
