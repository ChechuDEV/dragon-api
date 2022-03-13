package dev.chechu.dragonapi.core.commands;

import dev.chechu.dragonapi.core.utils.Description;
import dev.chechu.dragonapi.core.utils.Sender;

public interface Command {
    /**
     * Executes the command's code
     * @param sender Sender of the command
     * @param args Arguments of the command
     * @param commandManager Command manager
     */
    void execute(Sender<?> sender, String[] args, CommandManager<?> commandManager);
    Description getDescription();
}
