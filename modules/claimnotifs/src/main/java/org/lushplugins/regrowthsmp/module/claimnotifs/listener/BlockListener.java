package org.lushplugins.regrowthsmp.module.claimnotifs.listener;

import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.lushplugins.regrowthsmp.module.claimnotifs.ClaimNotifs;
import org.lushplugins.regrowthsmp.module.claimnotifs.config.ConfigManager;

public class BlockListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        this.checkAndNotify(block, ConfigManager.TriggerType.BLOCK_PLACE, event.getPlayer());
    }

    private void checkAndNotify(Block block, ConfigManager.TriggerType type, Player receiver) {
        Material material = block.getType();

        if (ClaimNotifs.getInstance().getConfigManager().getNotifications(type).contains(material)) {
            BukkitHuskClaimsAPI huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
            if (!huskClaimsAPI.isClaimAt(huskClaimsAPI.getPosition(block.getLocation()))) {
                ClaimNotifs.getInstance().getConfigManager().sendActionBarMessage(receiver, type, (msg) -> msg
                    .replace("%material%", material.name().replace("_", " ").toLowerCase()));
            }
        }
    }
}
