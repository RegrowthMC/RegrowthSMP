package org.lushplugins.regrowthsmp.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.lushplugins.lushlib.manager.Manager;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.module.ModuleManager;

import java.util.logging.Level;

public class ConfigManager extends Manager {

    public ConfigManager() {
        RegrowthSMP.getInstance().saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        RegrowthSMP plugin = RegrowthSMP.getInstance();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        ModuleManager moduleManager = plugin.getModuleManager();
        ConfigurationSection modulesSection = config.getConfigurationSection("modules");
        if (modulesSection != null) {
            modulesSection.getValues(false).forEach((moduleName, value) -> {
                String moduleId = moduleName.toLowerCase();

                if (!plugin.getModuleRegistry().isRegisteredModule(moduleId)) {
                    plugin.getLogger().log(Level.WARNING, moduleName + " is not a valid module type");
                    return;
                }

                if (value instanceof Boolean enable && enable) {
                    moduleManager.enableModule(moduleId);
                } else {
                    moduleManager.disableModule(moduleId);
                }
            });
        }
    }

    public void reload() {
        if (isEnabled()) {
            disable();
        }

        enable();
    }
}
