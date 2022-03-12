package dev.chechu.core.commands;

import dev.chechu.dragonapi.core.Configuration;
import dev.chechu.dragonapi.core.commands.Command;
import dev.chechu.dragonapi.core.commands.CommandManager;
import dev.chechu.dragonapi.core.utils.Description;
import dev.chechu.dragonapi.core.utils.Sender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {
    static CommandManager manager;
    static Sender<?> sender;
    static Command defCommand;
    static String out = "";
    static Configuration config;
    @BeforeAll
    public static void prepare() {
        config = Mockito.mock(Configuration.class);
        manager = new CommandManager(config);
        sender = Mockito.mock(Sender.class);

        defCommand = new Command() {
            @Override
            public void execute(Sender<?> sender, String[] args) {
                out = "Default command executed.";
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

    @Test
    public void assertThatDefaultExecutesWhenNonExistent() {
        out = "";
        manager.execute(sender, List.of("those").toArray(String[]::new),defCommand);
        assertEquals("Default command executed.",out, "Default should've executed.");
    }

    @Test
    public void assertThatDefaultDoesntExecuteWhenExistent() {
        out = "";
        manager.execute(sender, List.of("this").toArray(String[]::new),defCommand);
        assertNotEquals("Default command executed.",out,"Default shouldn't have been executed.");
    }

    @Test
    public void assertThatGetConfigWorks() {
        assertEquals(config,manager.getConfig(),"Config should be given.");
    }

}
