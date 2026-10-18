package org.lushplugins.regrowthsmp.hook.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextManager;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.hook.luckperms.context.BedrockPlayerCalculator;

public class LuckPermsHook {

    public static void registerContexts(RegrowthSMP plugin) {
        LuckPerms luckPerms = plugin.getServer().getServicesManager().load(LuckPerms.class);
        if (luckPerms == null) {
            throw new IllegalStateException("LuckPerms is required to register contexts");
        }

        ContextManager contextManager = luckPerms.getContextManager();
        plugin.ifPluginPresent("floodgate", () -> contextManager.registerCalculator(new BedrockPlayerCalculator()));
    }
}
