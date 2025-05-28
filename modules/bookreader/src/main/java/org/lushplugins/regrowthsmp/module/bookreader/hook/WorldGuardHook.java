package org.lushplugins.regrowthsmp.module.bookreader.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class WorldGuardHook {
    private static StateFlag BLOCK_READER_FLAG;

    public static boolean isRegionEnabled(@NotNull Block block) {
        return isRegionEnabled(block.getWorld(), block.getLocation());
    }

    public static boolean isRegionEnabled(@NotNull World world, @NotNull Location location) {
        return getRegionFlagState(world, location, BLOCK_READER_FLAG);
    }

    private static boolean getRegionFlagState(@NotNull World world, @NotNull Location location, @NotNull StateFlag flag) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            return true;
        }

        ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.adapt(location).toVector().toBlockPoint());
        List<ProtectedRegion> regions = set.getRegions().stream().sorted(Comparator.comparing(ProtectedRegion::getPriority)).toList();
        if (regions.isEmpty()) {
            return true;
        }

        ProtectedRegion region = regions.get(0);
        StateFlag.State state = region.getFlag(flag);
        return state == null || state.equals(StateFlag.State.ALLOW);
    }

    private static StateFlag registerStateFlag(@NotNull String name, boolean def) {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag(name, def);
            registry.register(flag);
            return flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get(name);
            return existing instanceof StateFlag flag ? flag : null;
        }
    }

    public static void prepare() {
        if (BLOCK_READER_FLAG == null) {
            BLOCK_READER_FLAG = registerStateFlag("book-reader", false);
        }
    }
}
