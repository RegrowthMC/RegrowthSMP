package org.lushplugins.regrowthsmp.module.abilities.ability;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.lushplugins.lushlib.utils.RandomCollection;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;
import org.lushplugins.regrowthsmp.module.abilities.data.AbilitiesUser;

import java.util.Random;

public class RainDanceAbility extends Ability implements Listener {
    private static final Random RANDOM = new Random();
    private static final RandomCollection<EntityType> RAIN_DROPS = new RandomCollection<>();

    static {
        RAIN_DROPS.add(EntityType.SALMON, 35);
        RAIN_DROPS.add(EntityType.COD, 35);
        RAIN_DROPS.add(EntityType.TROPICAL_FISH, 25);
        RAIN_DROPS.add(EntityType.PUFFERFISH, 0.5);
        RAIN_DROPS.add(EntityType.LIGHTNING_BOLT, 0.1);
    }

    public RainDanceAbility() {
        super(AbilityTypes.RAIN_DANCE);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }

        Player player = event.getPlayer();
        AbilitiesUser user = Abilities.getInstance().getCachedUserData(player.getUniqueId());
        if (user == null || user.getCurrentAbility() == null || !user.getCurrentAbility().equals(this.getId())) {
            return;
        }

        Location cloudLocation = player.getLocation().add(0, 5, 0);
        World world = player.getWorld();
        world.spawnParticle(Particle.RAIN, cloudLocation, 5, 1, 0.25, 1);

        int activeNearby = player.getWorld().getNearbyPlayers(player.getLocation(), 10, 5, 10, (otherPlayer) -> {
            AbilitiesUser otherUser = Abilities.getInstance().getCachedUserData(otherPlayer.getUniqueId());
            return otherUser != null && otherUser.getCurrentAbility() != null &&otherUser.getCurrentAbility().equals(this.getId());
        }).size();

        if (((activeNearby * activeNearby) / 2) + 5 > RANDOM.nextInt(0, 100)) {
            EntityType entityType = RAIN_DROPS.next();
            if (entityType == EntityType.LIGHTNING_BOLT) {
                world.strikeLightning(player.getLocation());
            } else {
                Location spawnLocation = cloudLocation.add(RANDOM.nextDouble(-1, 1), RANDOM.nextDouble(-1, 1), RANDOM.nextDouble());
                world.spawnEntity(spawnLocation, RAIN_DROPS.next());
            }
        }
    }
}
