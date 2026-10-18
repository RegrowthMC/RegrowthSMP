package org.lushplugins.regrowthsmp.command;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.chatcolorhandler.paper.PaperColor;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import revxrsal.commands.annotation.Command;

@SuppressWarnings("unused")
public class GiveCosmeticCommand {

    @Command("givecosmetic")
    public void giveCosmetic(Player player, CosmeticFormat format, String name) {
        ItemStack cosmetic = ItemStack.of(Material.LEATHER_HORSE_ARMOR);
        cosmetic.setData(DataComponentTypes.CUSTOM_NAME, PaperColor.handler().translate(format.formattedDisplayName(name)));
        cosmetic.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
            .addFloat(format.customModelData())
            .build());

        player.give(cosmetic);
        player.sendMessage(Component.text()
            .content("Given cosmetic to ")
            .color(TextColor.fromHexString("#b7faa2"))
            .append(Component.text()
                .content(player.getName())
                .color(TextColor.fromHexString("#66b04f")))
            .build());
    }

    public record CosmeticFormat(int customModelData, @Nullable String theme) {

        public String formattedDisplayName(String name) {
            return RegrowthSMP.getInstance().getCosmeticsConfig().getDisplayNameFormat(theme != null ? theme : "default")
                .replace("%display_name%", name);
        }
    }
}
