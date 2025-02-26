package org.lushplugins.regrowthsmp.module;

import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.cosmetics.Cosmetics;
import org.lushplugins.regrowthsmp.module.crateanimation.CrateAnimation;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;
import org.lushplugins.regrowthsmp.module.function.Functions;
import org.lushplugins.regrowthsmp.module.glassitemframes.GlassItemFrames;
import org.lushplugins.regrowthsmp.module.extraluckpermscontexts.ExtraLuckPermsContexts;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;
import org.lushplugins.regrowthsmp.module.recipes.Recipes;
import org.lushplugins.regrowthsmp.module.unbreakableblocks.UnbreakableBlocks;
import org.lushplugins.regrowthsmp.module.welcome.Welcome;

import java.util.function.Function;

@SuppressWarnings("Convert2MethodRef")
public enum ModuleType {
    ABILITIES((plugin) -> new Abilities(plugin)),
    COSMETICS((plugin) -> new Cosmetics(plugin)),
    CRATE_ANIMATION((plugin) -> new CrateAnimation(plugin)),
    FUNCTIONS((plugin) -> new Functions(plugin)),
    GLASS_ITEM_FRAMES((plugin) -> new GlassItemFrames(plugin)),
    LUCK_PERMS_CONTEXTS((plugin) -> new ExtraLuckPermsContexts(plugin)),
    PINATA((plugin) -> new Pinata(plugin)),
    RECIPES((plugin) -> new Recipes(plugin)),
    UNBREAKABLE_BLOCKS((plugin) -> new UnbreakableBlocks(plugin)),
    WELCOME((plugin) -> new Welcome(plugin));

    private final Function<RegrowthPlugin, Module> moduleCallable;

    ModuleType(Function<RegrowthPlugin, Module> moduleCallable) {
        this.moduleCallable = moduleCallable;
    }

    public Module init() {
        try {
            return moduleCallable.apply(RegrowthSMP.getInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
