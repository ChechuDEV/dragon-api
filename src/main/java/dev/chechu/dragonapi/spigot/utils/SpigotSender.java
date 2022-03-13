package dev.chechu.dragonapi.spigot.utils;

import dev.chechu.dragonapi.core.utils.Sender;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotSender extends Sender<CommandSender> {
    public SpigotSender(boolean consoleSender, boolean commandBlockSender, boolean playerSender, CommandSender sender) {
        super(consoleSender, commandBlockSender, playerSender, sender);
    }

    /**
     * Converts a CommandSender to a SpigotSender
     * @param sender Sender to be converted
     * @return A SpigotSender from the CommandSender
     */
    public static SpigotSender from(CommandSender sender) {
        boolean consoleSender = false;
        boolean commandBlockSender = false;
        boolean playerSender = false;
        if(sender instanceof ConsoleCommandSender) consoleSender = true;
        if(sender instanceof BlockCommandSender) commandBlockSender = true;
        if(sender instanceof Player) playerSender = true;
        return new SpigotSender(consoleSender, commandBlockSender, playerSender, sender);
    }

    /**
     * Sends a message to the sender
     * @param message Message to be sent
     */
    @Override
    public void sendMessage(@NotNull String message) {
        getSender().sendMessage(message);
    }


    public void sendMessage(@NotNull TextComponent component) {
        if(isPlayerSender())
            ((Player)getSender()).spigot().sendMessage(component);
    }

    /**
     * Displays an action bar if the Sender is a player
     * @param actionBar Action bar to be displayed
     */
    @Override
    public void sendActionBar(@NotNull String actionBar) {
        if(isPlayerSender())
            ((Player)getSender()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
    }

    /**
     * Shows a title to the sender if the sender is a player
     * @param title Title to be shown
     * @param subtitle Subtitle to be shown
     */
    @Override
    public void sendTitle(@NotNull String title, @Nullable String subtitle) {
        if(isPlayerSender())
            ((Player)getSender()).sendTitle(title,subtitle);
    }
}
