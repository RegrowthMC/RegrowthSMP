package org.lushplugins.regrowthsmp.module.pinata.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Pinata.getInstance().getBossBarManager().addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Pinata.getInstance().getBossBarManager().removePlayer(event.getPlayer());
    }
}
