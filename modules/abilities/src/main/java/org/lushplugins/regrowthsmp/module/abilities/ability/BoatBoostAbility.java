package org.lushplugins.regrowthsmp.module.abilities.ability;

import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;
import org.lushplugins.regrowthsmp.module.abilities.data.AbilitiesUser;

import java.util.HashSet;
import java.util.UUID;

public class BoatBoostAbility extends Ability implements Listener {
    private final HashSet<UUID> onCooldown = new HashSet<>();

    public BoatBoostAbility() {
        super(AbilityTypes.BOAT_BOOST);
    }

    @EventHandler
    public void onReelingIn(PlayerFishEvent event) {
        PlayerFishEvent.State state = event.getState();
        if (state != PlayerFishEvent.State.FAILED_ATTEMPT && state != PlayerFishEvent.State.REEL_IN) {
            return;
        }

        Player player = event.getPlayer();
        AbilitiesUser user = Abilities.getInstance().getCachedUserData(player.getUniqueId());
        if (user == null || user.getCurrentAbility() == null || !user.getCurrentAbility().equals(this.getId())) {
            return;
        }

        if (!player.isInsideVehicle()) {
            return;
        }

        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof Boat boat)) {
            return;
        }

        if (onCooldown.contains(player.getUniqueId())) {
            return;
        }

        if (!boat.isInWater() && boat.getWorld().isClearWeather()) {
            return;
        }

        // Handle applying cooldown
        UUID uuid = player.getUniqueId();
        onCooldown.add(uuid);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Abilities.getInstance().getPlugin(), () -> {
            onCooldown.remove(uuid);
        }, 20);

        FishHook hook = event.getHook();
        Vector direction = hook.getLocation().toVector().subtract(player.getLocation().toVector());
        boat.setVelocity(
            direction
                .normalize()
                .multiply(2)
                .setY(0.7)
        );
    }
}
