package org.lushplugins.regrowthsmp.command;

import io.github._4drian3d.jdwebhooks.Embed;
import io.github._4drian3d.jdwebhooks.WebHook;
import io.github._4drian3d.jdwebhooks.WebHookClient;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.lushplugins.chatcolorhandler.paper.PaperColor;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("unused")
public class ShareRewindCommand {
    private final Set<UUID> shared = new HashSet<>();

    @Command("sharerewind")
    @CommandPermission("regrowthsmp.sharerewind")
    public void shareRewind(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();
        if (shared.contains(player.getUniqueId())) {
            player.sendMessage(Component.text()
                .content("You have already shared your Regrowth Rewind!")
                .color(TextColor.fromHexString("#ff6969"))
                .build());
        }

        shared.add(player.getUniqueId());
        WebHookClient client = WebHookClient.fromURL(RegrowthSMP.getInstance().getMainConfig().getShareRewindWebHookUrl());
        Embed embed = Embed.builder()
            .color(0x9CF47A)
            .title(PaperColor.handler().translateRaw("%player_name%'s Season 1 Rewind", player))
            .field(Embed.Field.builder()
                .name("Playtime")
                .value(PaperColor.handler().translateRaw("""
                    First Joined: %player_first_join_date%
                    Total Playtime: %statistic_time_played%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Mining")
                .value(PaperColor.handler().translateRaw("""
                    Broken Pickaxes: %math_0:0_{statistic_break_item:wooden_pickaxe}+{statistic_break_item:stone_pickaxe}+{statistic_break_item:iron_pickaxe}+{statistic_break_item:golden_pickaxe}+{statistic_break_item:diamond_pickaxe}+{statistic_break_item:netherite_pickaxe}%
                    Diamond Ore Mined: %math_0:0_{statistic_mine_block:diamond_ore}+{statistic_mine_block:deepslate_diamond_ore}%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Skills")
                .value(PaperColor.handler().translateRaw("""
                    Average Skill Level: %ecoskills_average_skill_level%
                    Total Skill Levels: %ecoskills_total_skill_level%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Owchies")
                .value(PaperColor.handler().translateRaw("""
                    Distance Fallen: %math_0:0_{statistic_fall_one_cm}/100%
                    Damage Taken: %math_0:0_{statistic_damage_taken}/20%
                    Total Deaths: %statistic_deaths%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Green Hands")
                .value(PaperColor.handler().translateRaw("""
                    Animals Bred: %statistic_animals_bred%
                    Flowers Potted: %statistic_flower_potted%
                    Wheat Seeds Planted: %statistic_use_item:wheat_seeds%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Challenges")
                .value(PaperColor.handler().translateRaw("""
                    Completed Challenges: %excellentchallenges_completed_all%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Movement")
                .value(PaperColor.handler().translateRaw("""
                    Walked: %math_0:0_{statistic_walk_one_cm}/100%
                    Crouch Walked: %math_0:0_{statistic_crouch_one_cm}/100%
                    Swam: %math_0:0_{statistic_swim_one_cm}/100%
                    Climbed: %math_0:0_{statistic_climb_one_cm}/100%
                    Rowed Boat: %math_0:0_{statistic_boat_one_cm}/100%
                    Rode Horse: %math_0:0_{statistic_horse_one_cm}/100%
                    Rode Pig: %math_0:0_{statistic_pig_one_cm}/100%
                    """, player))
                .build())
            .build();

        WebHook webHook = WebHook.builder()
            .username("Regrowth Rewind")
            .embed(embed)
            .build();

        client.sendWebHook(webHook).thenAccept((ignored) -> {
            player.sendMessage(Component.text()
                .content("Your Regrowth Rewind has been shared to the discord!")
                .color(TextColor.fromHexString("#b7faa2"))
                .build());
        });
    }
}
