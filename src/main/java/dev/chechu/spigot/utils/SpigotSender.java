package dev.chechu.spigot.utils;

import dev.chechu.core.utils.Sender;
import lombok.Getter;
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

    public static SpigotSender from(CommandSender sender) {
        boolean consoleSender = false;
        boolean commandBlockSender = false;
        boolean playerSender = false;
        if(sender instanceof ConsoleCommandSender) consoleSender = true;
        if(sender instanceof BlockCommandSender) commandBlockSender = true;
        if(sender instanceof Player) playerSender = true;
        return new SpigotSender(consoleSender, commandBlockSender, playerSender, sender);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        getSender().sendMessage(message);
    }

    @Override
    public void sendActionBar(@NotNull String actionBar) {
        if(isPlayerSender())
            ((Player)getSender()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
    }

    @Override
    public void sendTitle(@NotNull String title, @Nullable String subtitle) {
        if(isPlayerSender())
            ((Player)getSender()).sendTitle(title,subtitle);
    }
}
