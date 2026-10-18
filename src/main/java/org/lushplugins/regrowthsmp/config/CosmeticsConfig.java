package org.lushplugins.regrowthsmp.config;

import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.regrowthsmp.RegrowthSMP;

import java.util.Collection;
import java.util.HashMap;

public class CosmeticsConfig {
    private HashMap<String, String> displayNameFormats;

    public CosmeticsConfig() {
        RegrowthSMP.getInstance().saveDefaultResource("cosmetics.yml");
    }

    public void reload() {
        ConfigurationSection config = RegrowthSMP.getInstance().getConfigResource("cosmetics.yml");

        this.displayNameFormats = new HashMap<>();
        ConfigurationSection themesSection = config.getConfigurationSection("display-name-themes");
        if (themesSection != null) {
            themesSection.getValues(false).forEach((key, value) -> displayNameFormats.put(key, (String) value));
        }
    }

    public String getDisplayNameFormat(String name) {
        return displayNameFormats.get(name);
    }

    public Collection<String> getDisplayNameFormats() {
        return displayNameFormats.keySet();
    }
}
