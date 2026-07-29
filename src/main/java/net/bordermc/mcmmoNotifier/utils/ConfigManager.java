package net.bordermc.mcmmoNotifier.utils;

import net.bordermc.mcmmoNotifier.McmmoNotifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class ConfigManager {
    private final McmmoNotifier plugin;

    private boolean enabled, richNotifier, autoDetectChannel;
    private String channel, message;
    private int frequencyLevels, limitLevels;

    public ConfigManager(@NotNull McmmoNotifier plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        enabled = config.getBoolean("enabled", true);
        richNotifier = config.getBoolean("richNotifier", true);
        autoDetectChannel = config.getBoolean("auto_detect_channel", false);
        channel = config.getString("channel", "general");
        frequencyLevels = config.getInt("frequency_levels", 100);
        limitLevels = config.getInt("limit_levels", -1);
        message = config.getString("message", "**%player%** reached **level %level%** in **%skill%**!");
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean richNotifier() {
        return richNotifier;
    }

    public boolean autoDetectChannel() {
        return autoDetectChannel;
    }

    public @NotNull String channel() {
        return channel;
    }

    public int frequencyLevels() {
        return frequencyLevels;
    }

    public int limitLevels() {
        return limitLevels;
    }

    public @NotNull String message() {
        return message;
    }
}
