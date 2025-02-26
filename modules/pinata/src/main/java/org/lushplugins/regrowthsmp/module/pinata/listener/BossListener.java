package org.lushplugins.regrowthsmp.module.pinata.listener;

import me.xemor.enchantedbosses.EnchantedBosses;
import me.xemor.enchantedbosses.SkillEntity;
import me.xemor.enchantedbosses.events.SkillEntitySpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;

public class BossListener implements Listener {

    @EventHandler
    public void onBossSpawn(SkillEntitySpawnEvent event) {
        LivingEntity entity = event.getEntity();

        if (!Pinata.getInstance().getConfigManager().getPinatas().contains(event.getSkillEntity().getName().toLowerCase())) {
            return;
        }

        int healthPerPlayer = Pinata.getInstance().getConfigManager().getHealthPerPlayer();
        int maxHealth = Pinata.getInstance().getConfigManager().getMaxHealth();
        int onlinePlayerCount = Bukkit.getOnlinePlayers().size();
        int health = Math.min(healthPerPlayer * onlinePlayerCount, maxHealth);

        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(health);
            entity.setHealth(health);
        }

        Pinata.getInstance().getBossBarManager().startBossBar(entity);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBossDamage(EntityDamageEvent event) {
        SkillEntity entity = EnchantedBosses.getInstance().getBossHandler().getBoss(event.getEntity());
        if (entity == null) {
            return;
        }

        if (!Pinata.getInstance().getConfigManager().getPinatas().contains(entity.getName().toLowerCase())) {
            return;
        }

        if (!(event.getDamageSource().getCausingEntity() instanceof Player)) {
            event.setCancelled(true);
        } else {
            event.setDamage(1);
        }
    }
}
