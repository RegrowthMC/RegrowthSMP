package org.lushplugins.regrowthsmp.hook.excellentcrates;

import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.hook.excellentcrates.listener.ExcellentCratesListener;
import org.lushplugins.regrowthsmp.hook.excellentcrates.opening.AnimatronicOpening;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.opening.OpeningManager;

import java.util.HashSet;

public final class ExcellentCratesHook {
    private final HashSet<String> lockedCrates = new HashSet<>();

    public ExcellentCratesHook() {
        OpeningManager openingManager = CratesAPI.getPlugin().getOpeningManager();

        if (RegrowthSMP.getInstance().getMainConfig().isFeatureEnabled("crate-animation")) {
            openingManager.loadProvider("regrowth-default", (player, source, key) -> {
                return new AnimatronicOpening(this, player, source, key);
            });

            RegrowthSMP.getInstance().registerListener(new ExcellentCratesListener(this));
        }
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
}
