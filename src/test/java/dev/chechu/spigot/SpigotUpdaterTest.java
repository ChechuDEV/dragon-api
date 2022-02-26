package dev.chechu.spigot;

import be.seeseemelk.mockbukkit.MockBukkit;
import dev.chechu.core.Updater;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpigotUpdaterTest {
    private static MockPlugin plugin;
    private static PluginDescriptionFile pdf;
    private static Server server;


    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
    }

    @Test
    public void assertThatNoNewVersionsExist() {
        plugin = MockBukkit.loadWith(MockPlugin.class,new PluginDescriptionFile("MockPlugin","2.1.1","dev.chechu.spigot.MockPlugin"));
        Updater updater = new SpigotUpdater(plugin, "63607",false);
        assertFalse(updater.checkForNewVersion());
    }

    @Test
    public void assertThatNewVersionExist() {
        plugin = MockBukkit.loadWith(MockPlugin.class,new PluginDescriptionFile("MockPlugin","2.1.0","dev.chechu.spigot.MockPlugin"));
        Updater updater = new SpigotUpdater(plugin, "63607",false);
        assertTrue(updater.checkForNewVersion());
    }

    @Test
    public void assertThatNewerVersionsArentDetected() {
        plugin = MockBukkit.loadWith(MockPlugin.class,new PluginDescriptionFile("MockPlugin","2.1.2","dev.chechu.spigot.MockPlugin"));
        Updater updater = new SpigotUpdater(plugin, "63607",false);
        assertFalse(updater.checkForNewVersion());
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }
}
