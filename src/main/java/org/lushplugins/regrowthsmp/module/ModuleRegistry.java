package org.lushplugins.regrowthsmp.module;

import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;
import org.lushplugins.regrowthsmp.module.bookreader.BookReader;
import org.lushplugins.regrowthsmp.module.cosmetics.Cosmetics;
import org.lushplugins.regrowthsmp.module.crateanimation.CrateAnimation;
import org.lushplugins.regrowthsmp.module.extraluckpermscontexts.ExtraLuckPermsContexts;
import org.lushplugins.regrowthsmp.module.function.Functions;
import org.lushplugins.regrowthsmp.module.glassitemframes.GlassItemFrames;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;
import org.lushplugins.regrowthsmp.module.unbreakableblocks.UnbreakableBlocks;
import org.lushplugins.regrowthsmp.module.utilities.Utilities;

import java.util.HashMap;
import java.util.function.Function;
import java.util.logging.Level;

public class ModuleRegistry {
    private final HashMap<String, Function<RegrowthPlugin, Module>> moduleTypes = new HashMap<>();

    @SuppressWarnings("Convert2MethodRef")
    public ModuleRegistry() {
        registerModule("abilities", (plugin) -> new Abilities(plugin));
        registerModule("book_reader", (plugin) -> new BookReader(plugin));
        registerModule("cosmetics", (plugin) -> new Cosmetics(plugin));
        registerModule("crate_animation", (plugin) -> new CrateAnimation(plugin));
        registerModule("functions", (plugin) -> new Functions(plugin));
        registerModule("glass_item_frames", (plugin) -> new GlassItemFrames(plugin));
        registerModule("luck_perms_contexts", (plugin) -> new ExtraLuckPermsContexts(plugin));
        registerModule("pinata", (plugin) -> new Pinata(plugin));
        registerModule("unbreakable_blocks", (plugin) -> new UnbreakableBlocks(plugin));
        registerModule("utilities", (plugin) -> new Utilities(plugin));
    }

    public boolean isRegisteredModule(String moduleId) {
        return moduleTypes.containsKey(moduleId);
    }

    public void registerModule(String moduleId, Function<RegrowthPlugin, Module> constructor) {
        moduleTypes.put(moduleId, constructor);
    }

    public Module constructModule(String moduleId) {
        Function<RegrowthPlugin, Module> constructor = moduleTypes.get(moduleId);

        try {
            return constructor != null ? constructor.apply(RegrowthSMP.getInstance()) : null;
        } catch (Exception e) {
            RegrowthSMP.getInstance().getLogger().log(Level.WARNING, "Caught error whilst constructing module '%s': ".formatted(moduleId), e);
            return null;
        }
    }
}
