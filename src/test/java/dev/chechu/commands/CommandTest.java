package dev.chechu.commands;

import dev.chechu.core.Configuration;
import dev.chechu.core.commands.Command;
import dev.chechu.core.commands.CommandManager;
import dev.chechu.core.utils.Description;
import dev.chechu.core.utils.Sender;
import dev.chechu.spigot.utils.SpigotSender;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandTest {
    static CommandManager manager;
    static Sender<?> sender;
    static Command defCommand;
    @BeforeAll
    public static void prepare() {
        manager = new CommandManager(Mockito.mock(Configuration.class));
        sender = SpigotSender.from(Mockito.mock(CommandSender.class));

        defCommand = new Command() {
            @Override
            public void execute(Sender<?> sender, String[] args) {
                System.out.println("Default command executed.");
            }

            @Override
            public Description getDescription() {
                return new Description("a","Another desc", Collections.emptyList(),Collections.emptyList());
            }
        };

        manager.addCommand(defCommand);

        Command command = new Command() {
            @Override
            public void execute(Sender<?> sender, String[] args) {
                if(args.length > 0 && args[0].equals("false")) {
                    System.out.println("False arg");
                    return;
                }
                if(args.length > 0 && args[0].equals("true")) {
                    System.out.println("True arg");
                    return;
                }
                System.out.println("No arg");
            }

            @Override
            public Description getDescription() {
                return new Description("this","Description",Collections.singletonList("param"),Collections.emptyList());
            }
        };
        manager.addCommand(command);
    }
    @Test
    @Order(1)
    public void addCommandTest() {
        Command testCommand = new Command() {
            @Override
            public void execute(Sender<?> sender, String[] args) {

            }

            @Override
            public Description getDescription() {
                return new Description("this","Description",Collections.singletonList("param"),Collections.emptyList());
            }
        };
        manager.addCommand(testCommand);

        assertTrue(manager.getCommandList().size() == 3 && manager.getCommandList().get(2) == testCommand, "Command should be added.");
    }

    @Test
    public void assertThatTrueCommandReturnsTrueWithoutArgs() {
        assertTrue(manager.call(sender,Collections.singletonList("this").toArray(String[]::new),null));
    }

    @Test
    public void assertThatTrueCommandReturnsTrueWithArgs() {
        assertTrue(manager.call(sender,Arrays.asList("this","true").toArray(String[]::new),null));
    }

    @Test
    public void assertThatTrueCommandReturnsFalseWithArgs() {
        assertTrue(manager.call(sender,Arrays.asList("this","false").toArray(String[]::new),null));
    }

    @Test
    public void assertThatNotExistentCommandReturnsFalse() {
        assertFalse(manager.call(sender, List.of("those").toArray(String[]::new),null));
    }



}
