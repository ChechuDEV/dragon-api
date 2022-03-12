package dev.chechu.spigot;

import be.seeseemelk.mockbukkit.MockBukkit;
import dev.chechu.dragonapi.core.Core;
import dev.chechu.dragonapi.core.Updater;
import dev.chechu.dragonapi.spigot.SpigotUpdater;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpigotUpdaterTest {
    private static MockPlugin plugin;
    private static Server server;


    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        new Core(true);
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

    @Test
    public void checkThatDownloads() {
        plugin = MockBukkit.loadWith(MockPlugin.class, new PluginDescriptionFile("MockPlugin","1.0.0","dev.chechu.spigot.MockPlugin"));
        Updater updater = new SpigotUpdater(plugin, "87154", true);
        updater.tryUpdate(false);
        File file = new File("out","CustomScoreboard-1.0.1.jar");
        assertTrue(file.exists(),"File should exist");
    }

    @SneakyThrows
    @AfterAll
    public static void tearDown() {
        FileUtils.cleanDirectory(new File("out"));
        MockBukkit.unmock();
    }
}
