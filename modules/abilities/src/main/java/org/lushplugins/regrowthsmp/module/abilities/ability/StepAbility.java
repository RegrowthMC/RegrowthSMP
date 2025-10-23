package org.lushplugins.regrowthsmp.module.abilities.ability;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.lushplugins.lushlib.registry.RegistryUtils;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;
import org.lushplugins.regrowthsmp.module.abilities.api.event.AbilitiesUserChangeAbilityEvent;
import org.lushplugins.regrowthsmp.module.abilities.data.AbilitiesUser;

public class StepAbility extends Ability implements Listener {

    public StepAbility() {
        super(AbilityTypes.STEP);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Abilities.getInstance().getPlugin(), () -> {
            Player player = event.getPlayer();
            AbilitiesUser user = Abilities.getInstance().getCachedUserData(player.getUniqueId());
            if (user == null || user.getCurrentAbility() == null || !user.getCurrentAbility().equals(this.getId())) {
                return;
            }

            applyAttribute(player);
        }, 40);
    }

    @EventHandler
    public void onAbilityChange(AbilitiesUserChangeAbilityEvent event) {
        if (event.getOldAbility() != null && event.getOldAbility().equals(this.getId())) {
            AbilitiesUser user = event.getUser();
            Player player = Bukkit.getPlayer(user.getUUID());
            if (player == null) {
                return;
            }

            removeAttribute(player);
        }

        if (event.getNewAbility() != null && event.getNewAbility().equals(this.getId())) {
            AbilitiesUser user = event.getUser();
            Player player = Bukkit.getPlayer(user.getUUID());
            if (player == null) {
                return;
            }

            applyAttribute(player);
        }
    }

    private void applyAttribute(Player player) {
        Attribute attribute = RegistryUtils.parseString("step_height", Registry.ATTRIBUTE);
        if (attribute == null) {
            return;
        }

        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            return;
        }

        attributeInstance.addTransientModifier(new AttributeModifier(
            new NamespacedKey(Abilities.getInstance().getPlugin(), "effects_step"),
            0.5,
            AttributeModifier.Operation.ADD_NUMBER
        ));
    }

    private void removeAttribute(Player player) {
        Attribute attribute = RegistryUtils.parseString("generic.step_height", Registry.ATTRIBUTE);
        if (attribute == null) {
            return;
        }

        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            return;
        }

        attributeInstance.removeModifier(new NamespacedKey(Abilities.getInstance().getPlugin(), "effects_step"));
    }
}
