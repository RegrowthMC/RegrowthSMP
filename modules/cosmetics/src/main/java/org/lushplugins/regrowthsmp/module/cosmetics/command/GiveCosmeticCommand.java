package org.lushplugins.regrowthsmp.module.cosmetics.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.lushlib.utils.DisplayItemStack;
import org.lushplugins.regrowthsmp.module.cosmetics.Cosmetics;

import java.util.Arrays;
import java.util.List;

public class GiveCosmeticCommand extends Command {

    public GiveCosmeticCommand() {
        super("givecosmetic");
        addRequiredPermission("regrowthsmp.cosmetics.give");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        String playerName = args[0];

        String[] dataArr = args[1].split(":");
        int customModelData = Integer.parseInt(dataArr[0]);
        String displayNameFormat = Cosmetics.getInstance().getConfigManager().getDisplayNameFormat(dataArr.length > 1 ? dataArr[1] : "default");
        String displayName = displayNameFormat.replace("%display_name%", String.join(" ", Arrays.copyOfRange(args, 2, args.length)));

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            ChatColorHandler.sendMessage(sender, String.format("&#ff6969Could not find &#d13636%s&#ff6969, are they online?", playerName));
            return true;
        }

        player.getInventory().addItem(DisplayItemStack.builder(Material.LEATHER_HORSE_ARMOR)
                .setCustomModelData(customModelData)
                .setDisplayName(displayName)
                .build()
                .asItemStack())
            .values()
            .forEach(itemStack -> player.getWorld().dropItem(player.getEyeLocation(), itemStack));
        ChatColorHandler.sendMessage(sender, "&#b7faa2Given cosmetic to &#66b04f" + playerName);
        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        return switch (fullArgs.length) {
            case 1 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case 2 -> {
                String currArg = fullArgs[1];
                if (currArg.isEmpty()) {
                    yield List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
                } else if (currArg.contains(":")) {
                    yield Cosmetics.getInstance().getConfigManager().getDisplayNameFormats().stream()
                        .map(format -> currArg.split(":")[0] + ":" + format)
                        .toList();
                } else {
                    yield List.of(currArg + ":");
                }
            }
            case 3 -> List.of("<display_name>");
            default -> null;
        };
    }
}
