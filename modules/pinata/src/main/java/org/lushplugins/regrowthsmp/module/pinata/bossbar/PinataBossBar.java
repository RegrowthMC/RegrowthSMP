package org.lushplugins.regrowthsmp.module.pinata.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;

import java.util.UUID;

public class PinataBossBar {
    private final NamespacedKey key;
    private final BossBar bossBar;
    private boolean active = true;

    public PinataBossBar(UUID entityUuid) {
        this.key = new NamespacedKey("pinata", entityUuid.toString());
        this.bossBar = Bukkit.createBossBar(
            this.key,
            ChatColorHandler.translate("&#FFDD55Pinata"),
            BarColor.YELLOW,
            BarStyle.SEGMENTED_10
        );

        Bukkit.getScheduler().runTaskTimer(Pinata.getInstance().getPlugin(), (task) -> {
            Entity entity = Bukkit.getEntity(entityUuid);
            if (!active || entity == null || !entity.isValid() || !(entity instanceof LivingEntity livingEntity)) {
                task.cancel();
                this.bossBar.removeAll();
                Bukkit.removeBossBar(this.key);
                Pinata.getInstance().getBossBarManager().removeBossBar(this.key);
                return;
            }

            double health = livingEntity.getHealth();
            double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double percentage = health / maxHealth;

            this.bossBar.setProgress(percentage);
        }, 0, 5);
    }

    public NamespacedKey getKey() {
        return this.key;
    }

    public void addPlayer(Player player) {
        this.bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        this.bossBar.removePlayer(player);
    }

    public void destroy() {
        this.active = false;
    }
}
