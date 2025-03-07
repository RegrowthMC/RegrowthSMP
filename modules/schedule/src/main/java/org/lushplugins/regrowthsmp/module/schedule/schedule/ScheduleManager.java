package org.lushplugins.regrowthsmp.module.schedule.schedule;

import com.google.common.collect.HashMultimap;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import org.lushplugins.regrowthsmp.module.schedule.Schedule;
import org.lushplugins.regrowthsmp.module.schedule.schedule.action.ScheduledAction;
import org.lushplugins.regrowthsmp.module.schedule.schedule.action.ScheduledCommands;

import java.time.Instant;
import java.util.*;

public class ScheduleManager {
    private final HashMultimap<Long, ScheduledCommands> scheduledActions = HashMultimap.create();
    private BukkitTask task;

    public void updateTask() {
        long epoch = Instant.now().getEpochSecond();
        runActionsBefore(epoch);
        unscheduleActionsBefore(epoch);

        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        long delay = getSecondsUntilNextAction();
        if (delay < 0) {
            return;
        }

        this.task = Bukkit.getScheduler().runTaskLater(
            Schedule.getInstance().getPlugin(),
            this::updateTask,
            delay * 20);
    }

    public void cancelTask() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    public Map<Long, Collection<ScheduledCommands>> getScheduleMap() {
        return scheduledActions.asMap();
    }

    public long getSecondsUntilNextAction() {
        OptionalLong triggerEpoch = this.scheduledActions.keySet().stream()
            .mapToLong(Long::longValue)
            .min();

        if (triggerEpoch.isPresent()) {
            return triggerEpoch.getAsLong() - Instant.now().getEpochSecond();
        } else {
            return -1;
        }
    }

    /**
     * @param epoch epoch seconds
     * @return a set of scheduled actions at the defined epoch
     */
    public Set<ScheduledCommands> getScheduledActionsAt(long epoch) {
        return this.scheduledActions.get(epoch);
    }

    /**
     * @param epoch epoch seconds to run actions at
     * @param action action to schedule
     */
    public void scheduleActionAt(long epoch, ScheduledCommands action) {
        this.scheduledActions.put(epoch, action);
        Bukkit.getScheduler().runTaskAsynchronously(Schedule.getInstance().getPlugin(), () -> ScheduleLoader.saveScheduledActions(epoch, getScheduledActionsAt(epoch)));
    }

    /**
     * @param epoch epoch seconds
     */
    public void unscheduleActionsAt(long epoch) {
        this.scheduledActions.removeAll(epoch);
        Bukkit.getScheduler().runTaskAsynchronously(Schedule.getInstance().getPlugin(), () -> ScheduleLoader.saveScheduledActions(epoch, null));
    }

    /**
     * @param epoch epoch seconds (inclusive)
     */
    public void unscheduleActionsBefore(long epoch) {
        for (Long triggerEpoch : getKeysBefore(epoch)) {
            this.scheduledActions.removeAll(triggerEpoch);
        }

        Bukkit.getScheduler().runTaskAsynchronously(Schedule.getInstance().getPlugin(), ScheduleLoader::saveAllScheduledActions);
    }

    public void clearActionsCache() {
        this.scheduledActions.clear();
    }

    /**
     * @param epoch epoch seconds
     */
    public void runActionsAt(long epoch) {
        getScheduledActionsAt(epoch).forEach(ScheduledAction::run);
    }

    /**
     * @param epoch epoch seconds (inclusive)
     */
    public void runActionsBefore(long epoch) {
        for (Long triggerEpoch : getKeysBefore(epoch)) {
            runActionsAt(triggerEpoch);
        }
    }

    /**
     * @param epoch epoch seconds
     */
    public void runAndRemoveActionsAt(long epoch) {
        runActionsAt(epoch);
        unscheduleActionsAt(epoch);
    }

    /**
     * @param epoch epoch seconds (inclusive)
     */
    public void runAndRemoveActionsBefore(long epoch) {
        for (Long triggerEpoch : getKeysBefore(epoch)) {
            runAndRemoveActionsAt(triggerEpoch);
        }
    }

    /**
     * @param epoch epoch seconds (inclusive)
     */
    private List<Long> getKeysBefore(long epoch) {
        return this.scheduledActions.keySet().stream()
            .filter(triggerEpoch -> triggerEpoch <= epoch)
            .toList();
    }
}
