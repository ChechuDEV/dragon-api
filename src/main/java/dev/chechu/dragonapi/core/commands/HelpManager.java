package dev.chechu.dragonapi.core.commands;

public class HelpManager {
    private final String mainCommand;
    private final CommandManager<?> commandManager;

    public HelpManager(String mainCommand, CommandManager<?> commandManager) {
        this.mainCommand = "/" + mainCommand;
        this.commandManager = commandManager;
    }

    /**
     * Gets help from the specified command, if specified it also maps the commands under it.
     * @param command Command from which to extract the help.
     * @param mapUnder Whether to map the commands under the specified command or not.
     * @return The help from the specified command.
     */
    public String getHelp(Command command, boolean mapUnder) {
        StringBuilder help = new StringBuilder();
        help.append(mainCommand + " ")
                .append(command.getDescription().getCommand()).append(" ")
                .append(getParams(command)).append("- ")
                .append(command.getDescription());
        if (mapUnder) {
            for (Command subcommand : command.getDescription().getSubcommands()) {
                help.append("\n")
                        .append(getHelp(subcommand, true));
            }
        }
        help.append("---");
        return String.valueOf(help);
    }

    /**
     * Gets the proper parameter containers.
     * @param command Command from which to extract parameters
     * @return Parameters between braces or smaller and bigger than symbols
     */
    public String getParams(Command command) {
        StringBuilder params = new StringBuilder();
        for (String param : command.getDescription().getParams()) {
            String parameter;
            if (param.startsWith("%o")) parameter = "["+param.substring(1)+"]";
            else parameter = "<"+param+">";
            params.append(parameter).append(" ");
        }
        return String.valueOf(params);
    }

    /**
     * Gets all the help
     * @return All commands help
     */
    public String getAllHelp() {
        StringBuilder help = new StringBuilder();
        for (Command command : commandManager.getCommandList()) {
            help.append(getHelp(command, true)).append("\n");
        }
        return String.valueOf(help);
    }
}
