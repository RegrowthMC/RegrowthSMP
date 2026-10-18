package org.lushplugins.regrowthsmp.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.lushplugins.regrowthsmp.RegrowthSMP;

public class Config {
    private String shareRewindWebHookUrl;

    public Config() {
        RegrowthSMP.getInstance().saveDefaultConfig();
    }

    public void reload() {
        RegrowthSMP plugin = RegrowthSMP.getInstance();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        this.shareRewindWebHookUrl = config.getString("share-rewind-webhook");
    }

    public String getShareRewindWebHookUrl() {
        return shareRewindWebHookUrl;
    }
}
