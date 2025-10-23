package org.lushplugins.regrowthsmp.module.abilities.hook;

import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.event.VulcanFlagEvent;
import org.bukkit.event.EventHandler;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;
import org.lushplugins.regrowthsmp.module.abilities.ability.OreCruncherAbility;
import org.lushplugins.regrowthsmp.module.abilities.ability.TreeChomperAbility;

import java.util.UUID;

public class VulcanAPIHook {

    public static void register() {
        Abilities.getInstance().getPlugin().registerListener(new Listener());
    }

    public static class Listener implements org.bukkit.event.Listener {

        @EventHandler
        public void onVulcanFlag(VulcanFlagEvent event) {
            Check check = event.getCheck();
            if (check.getName().equals("fastbreak") && check.getType() == 'a') {
                UUID uuid = event.getPlayer().getUniqueId();
                if (TreeChomperAbility.LumberTask.PLAYERS_LUMBERING.contains(uuid) || OreCruncherAbility.VeinMineTask.PLAYERS_CRUNCHING.contains(uuid)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
