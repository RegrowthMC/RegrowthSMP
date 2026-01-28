package org.lushplugins.regrowthsmp.module.pinata;

import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.pinata.bossbar.BossBarManager;
import org.lushplugins.regrowthsmp.module.pinata.command.PreparePinataCommand;
import org.lushplugins.regrowthsmp.module.pinata.command.SpawnPinataCommand;
import org.lushplugins.regrowthsmp.module.pinata.config.ConfigManager;
import org.lushplugins.regrowthsmp.module.pinata.listener.PlayerListener;

import java.util.Random;

public final class Pinata extends Module {
    private static final Random RANDOM = new Random();
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

        plugin.registerListener(new PlayerListener());

        plugin.registerCommand(new PreparePinataCommand());
        plugin.registerCommand(new SpawnPinataCommand());
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

    public static Random getRandom() {
        return RANDOM;
    }

    public static Pinata getInstance() {
        return instance;
    }
}
