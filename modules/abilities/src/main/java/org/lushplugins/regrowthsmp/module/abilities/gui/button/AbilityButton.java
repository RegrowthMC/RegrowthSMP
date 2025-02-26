package org.lushplugins.regrowthsmp.module.abilities.gui.button;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.skills.Skill;
import com.willfp.ecoskills.skills.Skills;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.lushplugins.lushlib.gui.button.ItemButton;
import org.lushplugins.lushlib.gui.inventory.Gui;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.lushlib.manager.GuiManager;
import org.lushplugins.lushlib.utils.DisplayItemStack;
import org.lushplugins.regrowthsmp.module.abilities.Abilities;
import org.lushplugins.regrowthsmp.module.abilities.data.AbilitiesUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AbilityButton extends ItemButton {
    private final String ability;
    private final String ecoSkill;
    private final DisplayItemStack item;

    public AbilityButton(String ability, String ecoSkill, Material material, List<String> description) {
        super((event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) {
                return;
            }

            if (!meetsSkillCriteria(player, ecoSkill)) {
                return;
            }

            UUID uuid = player.getUniqueId();
            AbilitiesUser user = Abilities.getInstance().getCachedUserData(uuid);
            if (user != null) {
                if (!player.hasPermission("regrowthsmp.abilities.bypasscooldown") && user.isOnAbilityChangeCooldown()) {
                    ChatColorHandler.sendMessage(player, String.format("&#ff6969You are on ability change cooldown for %s seconds", user.remainingAbilityChangeCooldown()));
                    return;
                }

                user.setCurrentAbility(ability);
                user.startAbilityChangeCooldown();

                player.playSound(player, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1f, 0.5f);

                Abilities.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(manager -> {
                    Gui gui = manager.getGui(uuid);
                    if (gui != null) {
                        gui.refresh();
                    }
                });
            }
        });

        this.ability = ability;
        this.ecoSkill = ecoSkill;
        this.item = DisplayItemStack.builder(material)
            .setDisplayName("&#FBC067&l" + makeFriendly(ability))
            .setLore(description.stream().map(line -> "&#A7A4A0" + line).toList())
            .build();
    }

    @Override
    public ItemStack getItemStack(Player player) {
        DisplayItemStack.Builder itemBuilder = DisplayItemStack.builder(this.item);
        List<String> lore = itemBuilder.hasLore() ? new ArrayList<>(itemBuilder.getLore()) : new ArrayList<>();

        if (meetsSkillCriteria(player, ecoSkill)) {
            AbilitiesUser user = Abilities.getInstance().getCachedUserData(player.getUniqueId());
            if (user != null) {
                lore.add(" ");
                lore.add("&#A7A4A0Active: &#FFD392" + (user.getCurrentAbility() != null && user.getCurrentAbility().equals(ability) ? "true" : "false"));
            }
        } else {
            lore.add(" ");
            lore.add(String.format("&#ff6969&oReach level 10 %s in /skills to unlock this", ecoSkill));
        }

        ItemStack item = DisplayItemStack.builder(this.item)
            .setLore(lore)
            .build()
            .asItemStack();

        item.addItemFlags(
            ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );

        return item;
    }

    private static boolean meetsSkillCriteria(Player player, String skillId) {
        Skill skill = Skills.INSTANCE.get(skillId);
        if (skill == null) {
            return false;
        }

        int skillLevel = EcoSkillsAPI.getSkillLevel(player, skill);
        return skillLevel >= 10;
    }

    private static String makeFriendly(String string) {
        StringBuilder output = new StringBuilder();

        String[] words = string.toLowerCase().split("[ _]");
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                output.append(" ");
            }

            String word = words[i];
            output.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
        }

        return output.toString();
    }
}
