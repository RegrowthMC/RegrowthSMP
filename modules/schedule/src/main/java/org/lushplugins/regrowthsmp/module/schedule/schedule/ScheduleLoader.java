package org.lushplugins.regrowthsmp.module.schedule.schedule;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.regrowthsmp.module.schedule.Schedule;
import org.lushplugins.regrowthsmp.module.schedule.schedule.action.ScheduledCommands;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScheduleLoader {
    private static final File DATA_FILE = new File(
        Schedule.getInstance().getPlugin().getDataFolder(),
        "modules/data/scheduled-actions.yml");

    public static void loadScheduledActions() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(DATA_FILE);

        ConfigurationSection actionsSection = config.getConfigurationSection("scheduled-actions");
        if (actionsSection == null) {
            return;
        }

        ScheduleManager scheduleManager = new ScheduleManager();
        for (Map.Entry<String, Object> entry : actionsSection.getValues(false).entrySet()) {
            long epoch = Long.parseLong(entry.getKey());
            ScheduledCommands action = new ScheduledCommands((List<String>) entry.getValue());

            scheduleManager.scheduleActionAt(epoch, action);
        }

        Bukkit.getScheduler().runTask(Schedule.getInstance().getPlugin(), scheduleManager::updateTask);
    }

    public static void saveScheduledActions(long epoch, @Nullable Set<ScheduledCommands> actions) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(DATA_FILE);

        if (actions == null) {
            config.set("scheduled-actions.%s".formatted(epoch), null);
        } else {
            Collection<String> commands = actions.stream()
                .flatMap(action -> action.getCommands().stream())
                .toList();

            config.set("scheduled-actions.%s".formatted(epoch), commands);
        }

        try {
            config.save(DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveAllScheduledActions() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(DATA_FILE);

        // Reset schedules section
        config.set("scheduled-actions", null);

        for (Map.Entry<Long, Collection<ScheduledCommands>> entry : Schedule.getInstance().getScheduleManager().getScheduleMap().entrySet()) {
            long epoch = entry.getKey();
            Collection<String> commands = entry.getValue().stream()
                .flatMap(action -> action.getCommands().stream())
                .toList();

            config.set("scheduled-actions.%s".formatted(epoch), commands);
        }

        try {
            config.save(DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
