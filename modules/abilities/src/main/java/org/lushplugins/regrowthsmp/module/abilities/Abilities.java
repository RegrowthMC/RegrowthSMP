package org.lushplugins.regrowthsmp.module.abilities;

import com.google.gson.JsonObject;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.abilities.ability.*;
import org.lushplugins.regrowthsmp.module.abilities.command.AbiltiesCommand;
import org.lushplugins.regrowthsmp.module.abilities.data.AbilitiesUser;
import org.lushplugins.regrowthsmp.module.abilities.hook.PlaceholderAPIHook;
import org.lushplugins.regrowthsmp.module.abilities.hook.VulcanAPIHook;

import java.util.HashMap;
import java.util.UUID;

public class Abilities extends Module {
    private static Abilities instance;

    private final HashMap<String, Ability> effects = new HashMap<>();

    public Abilities(RegrowthPlugin plugin) {
        super("abilities", plugin);

        if (instance == null) {
            instance = this;
        }

        registerEffect(new BetterPotionsAbility());
        registerEffect(new BoatBoostAbility());
        registerEffect(new ContactInvulnerabilityAbility());
        registerEffect(new FriendlyMobsAbility());
        registerEffect(new GrowthDanceAbility());
        registerEffect(new OreCruncherAbility());
        registerEffect(new RainDanceAbility());
        registerEffect(new StepAbility());
        registerEffect(new TreeChomperAbility());

        new PlaceholderAPIHook().enable();
        plugin.ifPluginPresent("Vulcan", VulcanAPIHook::register);

        plugin.registerCommand(new AbiltiesCommand());
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public boolean storesUserData() {
        return true;
    }

    @Override
    public AbilitiesUser createUserData(UUID uuid, JsonObject jsonObject) {
        return new AbilitiesUser(uuid, jsonObject);
    }

    @Override
    public @Nullable AbilitiesUser getCachedUserData(UUID uuid) {
        return (AbilitiesUser) super.getCachedUserData(uuid);
    }

    public void registerEffect(Ability ability) {
        Ability oldAbility = effects.get(ability.getId());
        if (oldAbility instanceof Listener oldListener) {
            // TODO: Add SpigotPlugin#unregisterListener
            HandlerList.unregisterAll(oldListener);
        }

        effects.put(ability.getId(), ability);
        if (ability instanceof Listener listener) {
            getPlugin().registerListener(listener);
        }
    }

    public static Abilities getInstance() {
        return instance;
    }
}
