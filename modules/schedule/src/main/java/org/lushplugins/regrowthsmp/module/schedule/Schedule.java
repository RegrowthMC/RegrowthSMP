package org.lushplugins.regrowthsmp.module.schedule;

import org.bukkit.Bukkit;
import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.schedule.command.ScheduleCommand;
import org.lushplugins.regrowthsmp.module.schedule.command.WithDelayedTimeCommand;
import org.lushplugins.regrowthsmp.module.schedule.schedule.ScheduleLoader;
import org.lushplugins.regrowthsmp.module.schedule.schedule.ScheduleManager;

public class Schedule extends Module {
    private static Schedule instance;

    private final ScheduleManager scheduleManager;

    public Schedule(RegrowthPlugin plugin) {
        super("schedule", plugin);

        if (instance == null) {
            instance = this;
        }

        this.scheduleManager = new ScheduleManager();

        plugin.registerCommand(new ScheduleCommand());
        plugin.registerCommand(new WithDelayedTimeCommand());
    }

    @Override
    protected void onEnable() {
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), ScheduleLoader::loadScheduledActions);
    }

    @Override
    protected void onDisable() {
        ScheduleLoader.saveAllScheduledActions();

        this.scheduleManager.cancelTask();
        this.scheduleManager.clearActionsCache();
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public static Schedule getInstance() {
        return instance;
    }
}
