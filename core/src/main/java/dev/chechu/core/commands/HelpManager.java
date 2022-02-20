package dev.chechu.core.commands;

public abstract class HelpManager {
    private String mainCommand;

    public HelpManager(String mainCommand) {
        this.mainCommand = "/" + mainCommand;
    }

    public void getHelp(Command command) {
        StringBuilder help = new StringBuilder();
        help.append(mainCommand + " ")
                .append(command.getDescription().getCommand()).append(" ")
                .append(getParams(command)).append("- ")
                .append(command.getDescription());
        for (Command subcommand : command.getSubcommands()) {
            help.append("\n")
                    .append(getHelp(subcommand));
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
        for (String param : command.getParams()) {
            String parameter;
            if (param.startsWith("%o")) parameter = "["+param.substring(1)+"]";
            else parameter = "<"+param+">";
            params.append(parameter).append(" ");
        }
        return String.valueOf(params);
    }

    /**
     * Gets all the help <br>TODO: Add a way to exclude, or only include, the specified command and its subcommands.
     * @return All commands help
     */
    public String getAllHelp() {
        StringBuilder help = new StringBuilder();
        for (Command command : getCommands()) {
            help.append(getHelp(command)).append("\n");
        }
        return String.valueOf(help);
    }
}
