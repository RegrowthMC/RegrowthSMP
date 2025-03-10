package org.lushplugins.regrowthsmp.module.schedule.schedule.action;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.lushplugins.regrowthsmp.module.schedule.Schedule;

import java.util.List;
import java.util.logging.Level;

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
            try {
                Bukkit.dispatchCommand(console, command);
            } catch (CommandException e) {
                Schedule.getInstance().getPlugin().getLogger().log(Level.WARNING, "Error occurred when executing command: ", e);
            }
        }
    }
}
