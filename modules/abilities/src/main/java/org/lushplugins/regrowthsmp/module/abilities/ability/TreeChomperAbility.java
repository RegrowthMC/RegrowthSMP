package org.lushplugins.regrowthsmp.module.abilities.ability;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.skills.Skill;
import com.willfp.ecoskills.skills.Skills;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TreeChomperAbility extends Ability implements Listener {

    public TreeChomperAbility() {
        super(AbilityTypes.TREE_CHOMPER);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
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
        if (!Tag.LOGS.isTagged(blockType)) {
            return;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!Tag.ITEMS_AXES.isTagged(mainHand.getType())) {
            return;
        }

        Skill skill = Skills.INSTANCE.get("woodcutting");
        if (skill == null) {
            return;
        }

        int breakLimit;
        int skillLevel = EcoSkillsAPI.getSkillLevel(player, skill);
        if (skillLevel < 10) {
            return;
        } else if (skillLevel < 15) {
            breakLimit = 16;
        } else if (skillLevel < 20) {
            breakLimit = 24;
        } else {
            breakLimit = 32;
        }

        new LumberTask(block, breakLimit, player);
    }

    public static class LumberTask {
        public static final Cache<UUID, Boolean> PLAYERS_LUMBERING = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();
        private static final int[] VERTICAL_BREAK_ORDER = new int[]{ 0, 1 };
        private static final int[][] BREAK_ORDER = new int[][]{
            { -1, 0 },
            { 0, 1 },
            { 1, 0 },
            { 0, -1 }
        };

        private final int breakLimit;
        private final Material blockType;
        private final Player player;
        private int blocksBroken = 0;

        public LumberTask(Block startBlock, int breakLimit, Player player) {
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
            PLAYERS_LUMBERING.put(uuid, true);
            boolean brokeBlock = player.breakBlock(block);

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
            Block aboveBlock = block.getRelative(BlockFace.UP);
            if (types.contains(aboveBlock.getType())) {
                return aboveBlock;
            }

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
