package org.lushplugins.regrowthsmp.module.schedule.schedule.action;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ScheduledCommands extends ScheduledAction {
    private final List<String> commands;

    public ScheduledCommands(List<String> commands) {
        this.commands = commands;
    }

    public List<String> getCommands() {
        return commands;
    }

    @Override
    public void run() {
        CommandSender console = Bukkit.getServer().getConsoleSender();
        for (String command : this.commands) {
            Bukkit.dispatchCommand(console, command);
        }
    }
}
