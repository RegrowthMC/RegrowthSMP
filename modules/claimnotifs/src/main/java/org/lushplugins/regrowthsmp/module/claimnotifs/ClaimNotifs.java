package org.lushplugins.regrowthsmp.module.claimnotifs;

import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.claimnotifs.config.ConfigManager;
import org.lushplugins.regrowthsmp.module.claimnotifs.listener.BlockListener;

public final class ClaimNotifs extends Module {
    private static ClaimNotifs instance;

    private final ConfigManager configManager;

    public ClaimNotifs(RegrowthPlugin plugin) {
        super("claim_notifs", plugin);

        if (instance == null) {
            instance = this;
        }

        this.configManager = new ConfigManager();

        plugin.registerListener(new BlockListener());
    }

    @Override
    public void onEnable() {
        configManager.reloadConfig();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static ClaimNotifs getInstance() {
        return instance;
    }
}
