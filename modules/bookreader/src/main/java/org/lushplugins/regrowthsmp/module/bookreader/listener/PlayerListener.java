package org.lushplugins.regrowthsmp.module.bookreader.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.lushplugins.regrowthsmp.module.bookreader.BookReader;
import org.lushplugins.regrowthsmp.module.bookreader.hook.WorldGuardHook;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        // Note: Ignore deprecation, ClickedPosition and InteractionPoint are not the same value
        Vector clickedPosition = event.getClickedPosition();
        if (block == null || clickedPosition == null) {
            return;
        }

        if (!(block.getState() instanceof ChiseledBookshelf bookshelf)) {
            return;
        }

        if (!WorldGuardHook.isRegionEnabled(block)) {
            return;
        }

        int clickedSlot = bookshelf.getSlot(clickedPosition);
        if (bookshelf.getInventory().getItem(clickedSlot) == null) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(BookReader.getInstance().getPlugin(), () -> {
            ItemStack item = bookshelf.getInventory().getItem(clickedSlot);
            if (item == null) {
                return;
            }

            event.getPlayer().openBook(item);
        }, 1);

    }
}
