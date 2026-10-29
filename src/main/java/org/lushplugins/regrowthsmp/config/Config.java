package org.lushplugins.regrowthsmp.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.lushplugins.lushlib.config.YamlUtils;
import org.lushplugins.regrowthsmp.RegrowthSMP;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {
    private String shareRewindWebHookUrl;
    private Set<String> features;

    public Config() {
        RegrowthSMP.getInstance().saveDefaultConfig();
    }

    public void reload() {
        RegrowthSMP plugin = RegrowthSMP.getInstance();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        this.shareRewindWebHookUrl = config.getString("share-rewind-webhook");

        this.features = YamlUtils.getMap(config, "features", Boolean.class).entrySet().stream()
            .filter(Map.Entry::getValue)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    public String getShareRewindWebHookUrl() {
        return shareRewindWebHookUrl;
    }

    public boolean isFeatureEnabled(String feature) {
        return this.features.contains(feature);
    }
}
