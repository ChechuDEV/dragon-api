package dev.chechu.spigot;

import dev.chechu.core.Configuration;
import dev.chechu.core.utils.ConfigChunk;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class SpigotConfig implements Configuration {
    @Getter private final JavaPlugin plugin;
    @Getter private final Logger logger;
    private FileConfiguration config;

    private List<ConfigChunk> configChunks = new ArrayList<>();

    private String currentVersion = "0.1";
    private String fileVersion = "0.1";

    public SpigotConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();
        config = plugin.getConfig();
        createFiles();
        if(fixVersions()) logger.info("Configuration has been fixed for you magically!");
    }

    @Override
    public void initializeConfig() {

    }

    @Override
    public void reloadConfig() {

    }

    @Override
    public void createFiles() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        plugin.saveDefaultConfig();
    }

    @Override
    public boolean fixVersions() {
        fileVersion = config.getString("version","-1");
        if(fileVersion.equals("-1")) {
            logger.warning("The configuration file may be too old or corrupted in order to infer its version. " +
                    "The plugin won't try to fix it and will try to read it anyways (Default values may be used).");
            return false;
        }
        boolean fixed = false;
        for (ConfigChunk<?> configChunk : configChunks) {
            if(!doesChunkExist(configChunk)) {
                config.set(configChunk.getKey(),configChunk.getDefaultValue());
                fixed = true;
            }
            setObjectFromChunk(configChunk);
        }
        if(fixed) plugin.saveConfig();
        return fixed;
    }

    @Override
    public <T> void setObjectFromChunk(ConfigChunk<T> chunk) {
        chunk.setObject(config.getObject(chunk.getKey(), chunk.getType(), chunk.getDefaultValue()));
    }

    @Override
    public <T> boolean doesChunkExist(ConfigChunk<T> chunk) {
        chunk.setObject(config.getObject(chunk.getKey(), chunk.getType(),null));
        return chunk.getObject() != null;
    }
}
