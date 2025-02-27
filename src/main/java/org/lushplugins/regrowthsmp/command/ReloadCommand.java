package org.lushplugins.regrowthsmp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.command.SubCommand;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.common.module.Module;

import java.util.ArrayList;
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
            String moduleId = args[0].toLowerCase();
            Module module =  RegrowthSMP.getInstance().getModuleManager().getModule(moduleId);
            if (module != null) {
                module.reload();
                ChatColorHandler.sendMessage(sender, "&#b7faa2RegrowthSMP has successfully reloaded &#66b04f%s 🔃".formatted(moduleId));
            } else {
                ChatColorHandler.sendMessage(sender, "&#ff6969Failed to find module &#d13636%s".formatted(moduleId));
            }
        } else {
            ChatColorHandler.sendMessage(sender, "&#b7faa2RegrowthSMP has been reloaded &#66b04f🔃");
        }

        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        if (args.length == 1) {
            return new ArrayList<>(RegrowthSMP.getInstance().getModuleManager().getModuleTypes());
        } else {
            return null;
        }
    }
}
