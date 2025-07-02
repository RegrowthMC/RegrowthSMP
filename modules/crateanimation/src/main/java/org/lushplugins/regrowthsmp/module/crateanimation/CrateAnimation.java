package org.lushplugins.regrowthsmp.module.crateanimation;

import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.crateanimation.listener.ExcellentCratesListener;
import org.lushplugins.regrowthsmp.module.crateanimation.opening.AnimatronicOpening;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.opening.OpeningManager;

import java.util.HashSet;

public final class CrateAnimation extends Module {
    private static CrateAnimation instance;

    private final HashSet<String> lockedCrates = new HashSet<>();

    public CrateAnimation(RegrowthPlugin plugin) {
        super("crate_animation", plugin);

        if (instance == null) {
            instance = this;
        }

        OpeningManager openingManager = CratesAPI.getPlugin().getOpeningManager();
        openingManager.loadProvider("regrowth-default", AnimatronicOpening::new);

        plugin.registerListeners(
            new ExcellentCratesListener()
        );
    }

    public boolean isCrateLocked(String crateName) {
        return lockedCrates.contains(crateName);
    }

    public void lockCrate(String crateName) {
        lockedCrates.add(crateName);
    }

    public void unlockCrate(String crateName) {
        lockedCrates.remove(crateName);
    }

    public static CrateAnimation getInstance() {
        return instance;
    }
}
