package org.lushplugins.regrowthsmp.module.pinata.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigManager {
    private int healthPerPlayer;
    private int maxHealth;
    private Location spawnLocation;
    private int spawnDuration;
    private List<String> pinatas;
    private Map<String, String> messages;
    private String discordWebhook;

    public ConfigManager() {
        Pinata.getInstance().getPlugin().saveDefaultResource("modules/pinata.yml");
    }

    public void reloadConfig() {
        ConfigurationSection config = Pinata.getInstance().getPlugin().getConfigResource("modules/pinata.yml");

        this.healthPerPlayer = config.getInt("health-per-player", 5);
        this.maxHealth = config.getInt("max-health", 250);
        this.spawnLocation = new Location(
            Bukkit.getWorld(config.getString("spawn-location.world", "world")),
            config.getDouble("spawn-location.x"),
            config.getDouble("spawn-location.y"),
            config.getDouble("spawn-location.z")
        );
        this.spawnDuration = config.getInt("spawn-duration", 30);
        this.pinatas = config.getStringList("pinatas");
        this.messages = config.getConfigurationSection("messages").getValues(false).entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> (String) entry.getValue()
            ));
        this.discordWebhook = config.getString("discord-webhook");
    }

    public int getHealthPerPlayer() {
        return healthPerPlayer;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getSpawnDuration() {
        return spawnDuration;
    }

    public List<String> getPinatas() {
        return pinatas;
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    public Optional<String> getOptionalMessage(String key) {
        return Optional.ofNullable(messages.get(key));
    }

    public void sendMessage(CommandSender sender, String key) {
        sendMessage(sender, key, (s) -> s);
    }

    public void sendMessage(CommandSender sender, String key, Function<String, String> parser) {
        String message = messages.get(key);
        if (message != null) {
            ChatColorHandler.sendMessage(sender, parser.apply(message));
        }
    }

    public String getDiscordWebhook() {
        return discordWebhook;
    }
}
