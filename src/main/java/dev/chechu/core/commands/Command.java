package dev.chechu.core.commands;

import dev.chechu.core.utils.Description;
import dev.chechu.core.utils.Sender;

public interface Command {
    /**
     * Executes the command's code
     * @param sender Sender of the command
     * @param args Arguments of the command
     */
    void execute(Sender<?> sender, String[] args);
    Description getDescription();
}
