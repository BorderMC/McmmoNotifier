package net.bordermc.mcmmoNotifier.listener;

import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import net.bordermc.mcmmoNotifier.utils.ConfigManager;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class McmmoNotifierListener implements Listener {
    private final ConfigManager config;

    public McmmoNotifierListener(@NotNull ConfigManager config) {
        this.config = config;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLevelUp(@NotNull McMMOPlayerLevelUpEvent event) {
        if (!config.enabled()) return;
        int newLevel = event.getSkillLevel() - event.getLevelsGained();
        if (newLevel > config.limitLevels()) return;
        if (newLevel % config.frequencyLevels() != 0) return;

        Player player = event.getPlayer();
        if (player.hasPermission("mcmmonotifier.do_not_notify")) return;
        PrimarySkillType skill = event.getSkill();
        int levelsGained = event.getLevelsGained();

        // Parse the placeholders. It can be done prettier, but it is what it is.
        String message = config.message()
                .replace("%player%", player.getName())
                .replace("%nickname%", PlainTextComponentSerializer
                        .plainText()
                        .serialize(player.displayName()))
                .replace("%new_level%", String.valueOf(newLevel))
                .replace("%levels_gained%", String.valueOf(levelsGained))
                .replace("%old_level%", String.valueOf(newLevel - levelsGained))
                .replace("%skill%", formatSkill(skill));

        // Broadcasts the message
        if (config.richNotifier()) {
            // Use DiscordSRV API to send a message
            TextChannel channel;
            if (config.autoDetectChannel()) {
                // Autodetect channel, usually general or global
                channel = DiscordSRV.getPlugin().getMainTextChannel();
            } else {
                channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(config.channel());
            }
            // Warning for an invalid/wrong DiscordSRV configuration
            // Not automatically falling back to autodetect to make sure the owner/developer sees that it's not working
            if (channel == null) {
                Bukkit.getLogger().severe("[McmmoNotifier] Channel configured or detected is wrong! Please set a valid channel in our config file or setup DiscordSRV correctly.");
                return;
            }
            channel.sendMessage(message).queue();
        } else {
            // This will just broadcast to the primary channel
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "discord broadcast " + message
            );
        }
    }

    private @NotNull String formatSkill(@NotNull PrimarySkillType skill) {
        String raw = skill.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
        String[] parts = raw.split(" ");
        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (!builder.isEmpty()) {
                builder.append(' ');
            }

            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }

        return builder.toString();
    }
}
