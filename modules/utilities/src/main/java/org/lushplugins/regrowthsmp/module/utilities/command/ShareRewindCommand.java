package org.lushplugins.regrowthsmp.module.utilities.command;

import io.github._4drian3d.jdwebhooks.Embed;
import io.github._4drian3d.jdwebhooks.WebHook;
import io.github._4drian3d.jdwebhooks.WebHookClient;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.regrowthsmp.module.utilities.Utilities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShareRewindCommand extends Command {
    private final Set<UUID> shared = new HashSet<>();

    public ShareRewindCommand() {
        super("sharerewind");
        addRequiredPermission("%s.utilities.sharerewind".formatted(Utilities.getInstance().getPlugin().getName().toLowerCase()));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        if (!(sender instanceof Player player)) {
            ChatColorHandler.sendMessage(sender, "&#ff6969This command can only be ran by players");
            return true;
        }

        if (shared.contains(player.getUniqueId())) {
            ChatColorHandler.sendMessage(sender, "&#ff6969You have already shared your Regrowth Rewind!");
            return true;
        }

        shared.add(player.getUniqueId());
        WebHookClient client = WebHookClient.fromURL(Utilities.getInstance().getPlugin().getConfig().getString("share-rewind-webhook"));
        Embed embed = Embed.builder()
            .color(0x9CF47A)
            .title(ChatColorHandler.translate("%player_name%'s Season 1 Rewind", player))
            .field(Embed.Field.builder()
                .name("Playtime")
                .value(ChatColorHandler.translate("""
                    First Joined: %player_first_join_date%
                    Total Playtime: %statistic_time_played%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Mining")
                .value(ChatColorHandler.translate("""
                    Broken Pickaxes: %math_0:0_{statistic_break_item:wooden_pickaxe}+{statistic_break_item:stone_pickaxe}+{statistic_break_item:iron_pickaxe}+{statistic_break_item:golden_pickaxe}+{statistic_break_item:diamond_pickaxe}+{statistic_break_item:netherite_pickaxe}%
                    Diamond Ore Mined: %math_0:0_{statistic_mine_block:diamond_ore}+{statistic_mine_block:deepslate_diamond_ore}%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Skills")
                .value(ChatColorHandler.translate("""
                    Average Skill Level: %ecoskills_average_skill_level%
                    Total Skill Levels: %ecoskills_total_skill_level%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Owchies")
                .value(ChatColorHandler.translate("""
                    Distance Fallen: %math_0:0_{statistic_fall_one_cm}/100%
                    Damage Taken: %math_0:0_{statistic_damage_taken}/20%
                    Total Deaths: %statistic_deaths%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Green Hands")
                .value(ChatColorHandler.translate("""
                    Animals Bred: %statistic_animals_bred%
                    Flowers Potted: %statistic_flower_potted%
                    Wheat Seeds Planted: %statistic_use_item:wheat_seeds%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Challenges")
                .value(ChatColorHandler.translate("""
                    Completed Challenges: %excellentchallenges_completed_all%
                    """, player))
                .inline(true)
                .build())
            .field(Embed.Field.builder()
                .name("Movement")
                .value(ChatColorHandler.translate("""
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
            ChatColorHandler.sendMessage(sender, "&#b7faa2Your Regrowth Rewind has been shared to the discord!");
        });

        return true;
    }
}
