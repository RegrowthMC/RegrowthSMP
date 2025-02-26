package org.lushplugins.regrowthsmp.module.pinata;

import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.pinata.bossbar.BossBarManager;
import org.lushplugins.regrowthsmp.module.pinata.config.ConfigManager;
import org.lushplugins.regrowthsmp.module.pinata.listener.BossListener;
import org.lushplugins.regrowthsmp.module.pinata.listener.PlayerListener;

public final class Pinata extends Module {
    private static Pinata instance;

    private final ConfigManager configManager;
    private final BossBarManager bossBarManager;

    public Pinata(RegrowthPlugin plugin) {
        super("pinata", plugin);

        if (instance == null) {
            instance = this;
        }

        this.configManager = new ConfigManager();
        this.bossBarManager = new BossBarManager();

        plugin.registerListener(new BossListener());
        plugin.registerListener(new PlayerListener());

        // TODO: Add "pinatacountdown" command (this could also be done in functions if a "wait" option was added)
    }

    @Override
    public void onEnable() {
        configManager.reloadConfig();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public static Pinata getInstance() {
        return instance;
    }
}
