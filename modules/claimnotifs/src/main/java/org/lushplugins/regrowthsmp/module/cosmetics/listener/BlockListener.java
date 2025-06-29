package org.lushplugins.regrowthsmp.module.cosmetics.listener;

import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.lushplugins.regrowthsmp.module.cosmetics.ClaimNotifs;
import org.lushplugins.regrowthsmp.module.cosmetics.config.ConfigManager;

public class BlockListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        this.checkAndNotify(block, ConfigManager.TriggerType.BLOCK_PLACE, event.getPlayer());
    }

    private void checkAndNotify(Block block, ConfigManager.TriggerType type, Player receiver) {
        Material material = block.getType();

        BukkitHuskClaimsAPI huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
        if (!huskClaimsAPI.isClaimAt(huskClaimsAPI.getPosition(block.getLocation()))) {
            if (ClaimNotifs.getInstance().getConfigManager().getNotifications(type).contains(material)) {
                ClaimNotifs.getInstance().getConfigManager().sendActionBarMessage(receiver, type, (msg) -> msg
                    .replace("%material%", material.name().replace("_", " ").toLowerCase()));
            }
        }
    }
}
