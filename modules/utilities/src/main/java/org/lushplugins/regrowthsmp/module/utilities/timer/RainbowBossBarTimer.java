package org.lushplugins.regrowthsmp.module.utilities.timer;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.plugin.Plugin;
import org.lushplugins.lushlib.timer.BossBarTimer;

public class RainbowBossBarTimer extends BossBarTimer {
    private static final BarColor[] COLORS = BarColor.values();

    private int colorIndex = 0;

    public RainbowBossBarTimer(String id, String title, BarStyle barStyle, Plugin plugin, int totalDuration) {
        super(id, title, BarColor.PINK, barStyle, plugin, totalDuration);
    }

    public RainbowBossBarTimer(String title, BarStyle barStyle, Plugin plugin, int totalDuration) {
        super(title, BarColor.PINK, barStyle, plugin, totalDuration);
    }

    @Override
    protected void onTick() {
        super.onTick();

        // Updates bar color every 5 ticks
        if (this.tick % 5 == 0) {
            this.getBossBar().setColor(this.nextColor());
        }
    }

    private BarColor nextColor() {
        return COLORS[++this.colorIndex % COLORS.length];
    }
}
