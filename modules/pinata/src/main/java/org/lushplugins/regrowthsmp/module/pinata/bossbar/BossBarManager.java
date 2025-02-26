package org.lushplugins.regrowthsmp.module.pinata.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossBarManager {
    private final HashMap<NamespacedKey, PinataBossBar> bossBars = new HashMap<>();

    public BossBarManager() {
        List<NamespacedKey> barsToRemove = new ArrayList<>();

        Bukkit.getBossBars().forEachRemaining(bossBar -> {
            NamespacedKey key = bossBar.getKey();
            if (key.namespace().equals("pinata")) {
                barsToRemove.add(key);
            }
        });

        for (NamespacedKey barToRemove : barsToRemove) {
            Bukkit.removeBossBar(barToRemove);
        }
    }

    public void startBossBar(LivingEntity entity) {
        PinataBossBar bossBar = new PinataBossBar(entity.getUniqueId());
        addBossBar(bossBar);

        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
    }

    public void addBossBar(PinataBossBar bossBar) {
        NamespacedKey key = bossBar.getKey();
        if (bossBars.containsKey(key)) {
            bossBars.get(key).destroy();
        }

        bossBars.put(bossBar.getKey(), bossBar);
    }

    public void removeBossBar(NamespacedKey key) {
        bossBars.remove(key);
    }

    public void addPlayer(Player player) {
        for (PinataBossBar bossBar : bossBars.values()) {
            bossBar.addPlayer(player);
        }
    }

    public void removePlayer(Player player) {
        for (PinataBossBar bossBar : bossBars.values()) {
            bossBar.removePlayer(player);
        }
    }
}
