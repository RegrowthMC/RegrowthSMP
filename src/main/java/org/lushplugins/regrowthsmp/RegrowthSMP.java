package org.lushplugins.regrowthsmp;

import org.lushplugins.lushlib.LushLib;
import org.lushplugins.regrowthsmp.command.RegrowthSMPCommand;
import org.lushplugins.regrowthsmp.data.SMPUser;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.config.ConfigManager;
import org.lushplugins.regrowthsmp.data.UserManager;
import org.lushplugins.regrowthsmp.listener.PlayerListener;
import org.lushplugins.regrowthsmp.module.ModuleManager;
import org.lushplugins.regrowthsmp.module.ModuleRegistry;
import org.lushplugins.regrowthsmp.storage.StorageManager;

import java.util.UUID;

public final class RegrowthSMP extends RegrowthPlugin {
    private static RegrowthSMP plugin;

    private ModuleRegistry moduleRegistry;
    private StorageManager storageManager;
    private UserManager userManager;
    private ModuleManager moduleManager;
    private ConfigManager configManager;

    @Override
    public void onLoad() {
        plugin = this;
        LushLib.getInstance().enable(this);
        moduleRegistry = new ModuleRegistry();
    }

    @Override
    public void onEnable() {
        storageManager = new StorageManager();
        userManager = new UserManager();
        moduleManager = new ModuleManager();

        configManager = new ConfigManager();
        configManager.reload();

        registerListener(new PlayerListener());

        registerCommand(new RegrowthSMPCommand());
    }

    @Override
    public void onDisable() {
        if (configManager != null) {
            configManager.disable();
            configManager = null;
        }

        if (moduleManager != null) {
            moduleManager.disable();
            moduleManager = null;
        }

        if (userManager != null) {
            userManager = null;
        }

        if (storageManager != null) {
            storageManager.disable();
            storageManager = null;
        }
    }

    public ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    @Override
    public SMPUser getCachedSMPUser(UUID uuid) {
        return userManager.getCachedUser(uuid);
    }

    @Override
    public void saveCachedSMPUser(UUID uuid) {
        userManager.saveUser(this.getCachedSMPUser(uuid));
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static RegrowthSMP getInstance() {
        return plugin;
    }


}
