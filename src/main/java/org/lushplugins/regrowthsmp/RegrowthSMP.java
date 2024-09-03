package org.lushplugins.regrowthsmp;

import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.regrowthsmp.module.crateanimation.CrateAnimation;

public final class RegrowthSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new CrateAnimation();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
