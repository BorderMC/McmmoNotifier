package net.bordermc.mcmmoNotifier.command;

import net.bordermc.mcmmoNotifier.utils.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class McmmoNotifierCommand implements CommandExecutor {
    private final ConfigManager config;

    public McmmoNotifierCommand(@NotNull ConfigManager config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            config.reload();
            sender.sendMessage(Component.text("Mcmmonotifier configuration reloaded.", NamedTextColor.GREEN));
            return true;
        }

        sender.sendMessage(Component.text("Usage: /" + label + " reload", NamedTextColor.RED));
        return true;
    }
}
