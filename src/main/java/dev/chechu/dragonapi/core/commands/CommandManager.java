package dev.chechu.dragonapi.core.commands;

import dev.chechu.dragonapi.core.Configuration;
import dev.chechu.dragonapi.core.utils.Sender;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages the plugins commands and its executions.
 * @param <T> Configuration class, must extend dev.chechu.dragonapi.core.Configuration class or subclasses
 */
public class CommandManager<T extends Configuration<?>> {
    @Getter private final List<Command> commandList = new ArrayList<>();
    @Getter final private T config;
    @Getter final private HelpManager helpManager;

    public CommandManager(T config, String mainCommand) {
        this.config = config;
        this.helpManager = new HelpManager(mainCommand,this);
    }

    /**
     * Adds command to the command list
     * @param command Command to be added
     */
    public void addCommand(@NotNull Command command) {
        commandList.add(command);
    }

    /**
     * Tries to find the first command, if no command has been found, defaultCommand will be executed.
     * @param sender Sender of the command.
     * @param args Arguments array given by the sender.
     * @param defaultCommand Default command.
     */
    public void execute(@NotNull Sender<?> sender, @NotNull String[] args, Command defaultCommand) {
        if(!call(sender, args, null)) defaultCommand.execute(sender, args, this);
    }

    /**
     * Recursively checks for commands matching arguments and executes the last command found.
     * @param sender Sender of the command.
     * @param args Arguments array given by the sender.
     * @param topCommand Last command found.
     * @return Whether a command has been found further from topCommand or not.
     */
    public boolean call(@NotNull Sender<?> sender, @NotNull String[] args, @Nullable Command topCommand) {
        for (Command command : topCommand == null ? getCommandList() : topCommand.getDescription().getSubcommands()) {
            if (args.length == 0) return false;
            if (command.getDescription().getCommand().equals(args[0])) {
                if(!call(sender, nextArgs(args),command)) {
                    command.execute(sender, nextArgs(args),this);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes the first argument, which is usually a command and isn't needed for the requested command to be executed.
     * @param args The arguments array.
     * @return The same array but without the first argument.
     */
    private @NotNull String[] nextArgs(@NotNull String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
