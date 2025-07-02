package org.lushplugins.regrowthsmp.module.crateanimation.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.lushplugins.regrowthsmp.module.crateanimation.CrateAnimation;
import org.lushplugins.regrowthsmp.module.crateanimation.opening.AnimatronicOpening;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.api.event.CrateOpenEvent;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.opening.OpeningManager;

import java.util.Objects;

public class ExcellentCratesListener implements Listener {

    @EventHandler
    public void onCrateOpen(CrateOpenEvent event) {
        this.verifyProviders();

        Crate crate = event.getCrate();
        if (!Objects.equals(crate.getName(), "regrowth-default")) {
            return;
        }

        String crateName = crate.getName();
        if (CrateAnimation.getInstance().isCrateLocked(crateName)) {
            event.setCancelled(true);
        } else {
            CrateAnimation.getInstance().lockCrate(crateName);
        }
    }

    // TODO: Remove when ExcellentCrates adds proper reload impl
    private void verifyProviders() {
        OpeningManager openingManager = CratesAPI.getPlugin().getOpeningManager();

        if (openingManager.getProviderById("regrowth-default") == null) {
            openingManager.loadProvider("regrowth-default", AnimatronicOpening::new);
        }
    }
}