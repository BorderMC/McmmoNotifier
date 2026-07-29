package net.bordermc.mcmmoNotifier;

import net.bordermc.mcmmoNotifier.command.McmmoNotifierCommand;
import net.bordermc.mcmmoNotifier.listener.McmmoNotifierListener;
import net.bordermc.mcmmoNotifier.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class McmmoNotifier extends JavaPlugin {
    private final ConfigManager config;

    public McmmoNotifier() {
        this.config = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("mcMMO") == null || !Bukkit.getPluginManager().isPluginEnabled("mcMMO")) {
            getLogger().info("[McmmoNotifier] McMMO plugin not found; Plugin will stay idle.");
            return;
        }

        Objects.requireNonNull(getCommand("mcmmonotifier"), "Command 'mcmmonotifier' is not defined in plugin.yml")
                .setExecutor(new McmmoNotifierCommand(config));
        getServer().getPluginManager().registerEvents(
                new McmmoNotifierListener(config), this
        );
    }
}
