package org.lushplugins.regrowthsmp.module.abilities.ability;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.skills.Skill;
import com.willfp.ecoskills.skills.Skills;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.regrowthsmp.module.abilities.data.AbilitiesUser;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class OreCruncherAbility extends Ability implements Listener {
    private static final List<Material> ORES = List.of(
        Material.COAL_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.COPPER_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.IRON_ORE,
        Material.DEEPSLATE_IRON_ORE,
        Material.GOLD_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.REDSTONE_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.EMERALD_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.LAPIS_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DIAMOND_ORE,
        Material.DEEPSLATE_DIAMOND_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE
    );

    public OreCruncherAbility() {
        super(AbilityTypes.ORE_CRUNCHER);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())  {
            return;
        }

        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return;
        }

        AbilitiesUser user = Abilities.getInstance().getCachedUserData(player.getUniqueId());
        if (user == null || user.getCurrentAbility() == null || !user.getCurrentAbility().equals(this.getId())) {
            return;
        }

        Block block = event.getBlock();
        Material blockType = block.getType();
        if (!ORES.contains(blockType)) {
            return;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!Tag.ITEMS_PICKAXES.isTagged(mainHand.getType())) {
            return;
        }

        Skill skill = Skills.INSTANCE.get("mining");
        if (skill == null) {
            return;
        }

        int breakLimit;
        int skillLevel = EcoSkillsAPI.getSkillLevel(player, skill);
        if (skillLevel < 10) {
            return;
        } else if (skillLevel < 15) {
            breakLimit = 6;
        } else if (skillLevel < 20) {
            breakLimit = 8;
        } else {
            breakLimit = 12;
        }

        new VeinMineTask(block, breakLimit, player);
    }

    public static class VeinMineTask {
        public static final HashSet<UUID> PLAYERS_CRUNCHING = new HashSet<>();
        private static final int[] VERTICAL_BREAK_ORDER = new int[]{ 0, -1, 1 };
        private static final int[][] BREAK_ORDER = new int[][]{
            { 0, 0 },
            { -1, 0 },
            { 0, 1 },
            { 1, 0 },
            { 0, -1 },
            { -1, -1 },
            { -1, 1 },
            { 1, 1 },
            { 1, -1 }
        };

        private final int breakLimit;
        private final Material blockType;
        private final Player player;
        private int blocksBroken = 0;

        public VeinMineTask(Block startBlock, int breakLimit, Player player) {
            this.breakLimit = breakLimit;
            this.blockType = startBlock.getType();
            this.player = player;

            startTask(startBlock);
        }

        private void startTask(Block block) {
            Block nextBlock = findNextBlock(block, blockType);
            if (nextBlock != null) {
                Bukkit.getScheduler().runTaskLater(Abilities.getInstance().getPlugin(), () -> {
                    breakConnectedBlocks(nextBlock);
                }, 5);
            }
        }

        private void breakConnectedBlocks(@NotNull Block block) {
            Location location = block.getLocation();
            World world = block.getWorld();

            if (blocksBroken >= breakLimit) {
                return;
            }

            // Handle block break
            UUID uuid = player.getUniqueId();
            PLAYERS_CRUNCHING.add(uuid);
            boolean brokeBlock = player.breakBlock(block);
            PLAYERS_CRUNCHING.remove(uuid);

            if (!brokeBlock) {
                return;
            }

            blocksBroken += 1;

            // Handle sounds/particles
            BlockData blockData = blockType.createBlockData();
            world.playSound(location.clone().add(0.5, 0.5, 0.5), blockData.getSoundGroup().getBreakSound(), 1f, 1f);
            world.spawnParticle(Particle.BLOCK, location.clone().add(0.5, 0.5, 0.5), 50, 0.3, 0.3, 0.3, blockData);

            // Extra check ran prior to finding next block to slightly improve performance
            if (blocksBroken >= breakLimit) {
                return;
            }

            // Find the next block to break and schedule
            Block nextBlock = findNextBlock(block, blockType);
            if (nextBlock != null) {
                Bukkit.getScheduler().runTaskLater(Abilities.getInstance().getPlugin(), () -> {
                    breakConnectedBlocks(nextBlock);
                }, 5);
            }
        }

        public static Block findNextBlock(Block block, Material type) {
            return findNextBlock(block, Collections.singletonList(type));
        }

        public static Block findNextBlock(Block block, List<Material> types) {
            Location startLocation = block.getLocation();

            for (int y : VERTICAL_BREAK_ORDER) {
                for (int[] coords : BREAK_ORDER) {
                    int x = coords[0];
                    int z = coords[1];

                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    Location currLoc = startLocation.clone().add(x, y, z);
                    Block nextBlock = currLoc.getBlock();
                    if (types.contains(nextBlock.getType())) {
                        return nextBlock;
                    }
                }
            }

            return null;
        }
    }
}
