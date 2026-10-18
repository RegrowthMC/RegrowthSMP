package org.lushplugins.regrowthsmp;

import org.bukkit.boss.BarColor;
import org.lushplugins.lushlib.utils.plugin.SpigotPlugin;
import org.lushplugins.regrowthsmp.command.GiveCosmeticCommand;
import org.lushplugins.regrowthsmp.command.RegrowthSMPCommand;
import org.lushplugins.regrowthsmp.command.ShareRewindCommand;
import org.lushplugins.regrowthsmp.command.TimerCommand;
import org.lushplugins.regrowthsmp.command.suggest.TimerColor;
import org.lushplugins.regrowthsmp.config.Config;
import org.lushplugins.regrowthsmp.config.CosmeticsConfig;
import org.lushplugins.regrowthsmp.hook.excellentcrates.ExcellentCratesHook;
import org.lushplugins.regrowthsmp.hook.luckperms.LuckPermsHook;
import org.lushplugins.regrowthsmp.hook.worldguard.WorldGuardHook;
import org.lushplugins.regrowthsmp.hook.worldguard.listener.BookReaderListener;
import revxrsal.commands.bukkit.BukkitLamp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class RegrowthSMP extends SpigotPlugin {
    private static RegrowthSMP plugin;

    private Config config;
    private CosmeticsConfig cosmeticsConfig;

    @Override
    public void onLoad() {
        plugin = this;

        ifPluginPresent("WorldGuard", WorldGuardHook::registerFlags);
    }

    @Override
    public void onEnable() {
        config = new Config();
        config.reload();

        cosmeticsConfig = new CosmeticsConfig();
        cosmeticsConfig.reload();

        //noinspection Convert2MethodRef
        ifPluginPresent("ExcellentCrates", () -> new ExcellentCratesHook());
        ifPluginPresent("LuckPerms", () -> LuckPermsHook.registerContexts(this));
        ifPluginPresent("WorldGuard", () -> registerListener(new BookReaderListener()));

        BukkitLamp.builder(this)
            .parameterTypes(types -> types
                .addParameterType(GiveCosmeticCommand.CosmeticFormat.class, (input, context) -> {
                    String[] data = input.readString().split(":");
                    int customModelData = Integer.parseInt(data[0]);
                    String theme = data.length > 1 ? data[1] : "default";
                    
                    return new GiveCosmeticCommand.CosmeticFormat(customModelData, theme);
                }))
            .suggestionProviders(providers -> providers
                .addProvider(GiveCosmeticCommand.CosmeticFormat.class, (context) -> {
                    String argument = context.input().peekString();
                    if (argument.isEmpty()) {
                        return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
                    } else if (argument.contains(":")) {
                        return RegrowthSMP.getInstance().getCosmeticsConfig().getDisplayNameFormats().stream()
                            .map(format -> argument.split(":")[0] + ":" + format)
                            .toList();
                    } else {
                        return List.of(argument + ":");
                    }
                })
                .addProviderForAnnotation(TimerColor.class, (_) -> (context) -> {
                    List<String> results = Arrays.stream(BarColor.values())
                        .map(barColor -> barColor.name().toLowerCase())
                        .collect(Collectors.toList());
                    results.add("rainbow");
                    return results;
                }))
            .build()
            .register(
                new GiveCosmeticCommand(),
                new RegrowthSMPCommand(),
                new ShareRewindCommand(),
                new TimerCommand()
            );
    }

    public Config getMainConfig() {
        return config;
    }

    public CosmeticsConfig getCosmeticsConfig() {
        return cosmeticsConfig;
    }

    public static RegrowthSMP getInstance() {
        return plugin;
    }
}
