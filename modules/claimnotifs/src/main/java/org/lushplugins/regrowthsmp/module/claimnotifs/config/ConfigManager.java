package org.lushplugins.regrowthsmp.module.claimnotifs.config;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.lushlib.registry.RegistryUtils;
import org.lushplugins.regrowthsmp.module.claimnotifs.ClaimNotifs;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConfigManager {
    private Map<TriggerType, List<Material>> notifications;
    private Map<String, String> messages;

    public ConfigManager() {
        ClaimNotifs.getInstance().getPlugin().saveDefaultResource("modules/claim-notifs.yml");
    }

    public void reloadConfig() {
        ConfigurationSection config = ClaimNotifs.getInstance().getPlugin().getConfigResource("modules/claim-notifs.yml");

        this.notifications = new HashMap<>();
        ConfigurationSection notificationsSection = config.getConfigurationSection("notifications");
        if (notificationsSection != null) {
            notificationsSection.getKeys(false).forEach(key -> {
                TriggerType type = TriggerType.valueOf(key.replace("-", "_").toUpperCase());
                List<Material> materials = notificationsSection.getStringList(key).stream()
                    .map(value -> RegistryUtils.parseString(value, Registry.MATERIAL))
                    .toList();

                this.notifications.put(type, materials);
            });
        }

        this.messages = new HashMap<>();
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            messagesSection.getValues(false).forEach((key, value) -> this.messages.put(key, (String) value));
        }
    }

    public @NotNull List<Material> getNotifications(TriggerType type) {
        List<Material> materials = this.notifications.get(type);
        return materials != null ? materials : Collections.emptyList();
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    public String getMessage(TriggerType type) {
        String friendlyName = type.name().replace("_", "-").toLowerCase();
        return getMessage(friendlyName);
    }

    public void sendMessage(CommandSender receiver, TriggerType type) {
        ChatColorHandler.sendMessage(receiver, this.getMessage(type));
    }

    public void sendActionBarMessage(Player receiver, TriggerType type, Function<String, String> mapper) {
        ChatColorHandler.sendActionBarMessage(receiver, mapper.apply(this.getMessage(type)));
    }

    public enum TriggerType {
        BLOCK_PLACE
    }
}
