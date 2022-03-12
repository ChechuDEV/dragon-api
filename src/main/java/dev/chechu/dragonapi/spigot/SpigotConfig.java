package dev.chechu.dragonapi.spigot;

import dev.chechu.dragonapi.core.Configuration;
import dev.chechu.dragonapi.core.utils.ConfigChunk;
import dev.chechu.dragonapi.core.utils.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class SpigotConfig extends Configuration {
    @Getter private final JavaPlugin plugin;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private FileConfiguration config;


    public SpigotConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        setLogger(plugin.getLogger());
        config = plugin.getConfig();
        createFiles();
        if(fixVersions()) getLogger().info(Message.get("config_fix"));
    }

    @Override
    public void createFiles() {
        if (!plugin.getDataFolder().exists()) if(!plugin.getDataFolder().mkdir()) {
            getLogger().severe(Message.get("config_folder_creation_error"));
        }
        plugin.saveDefaultConfig();
        File configFile = new File(plugin.getDataFolder() + "config.yml");
        if (!configFile.exists()) getLogger().severe(Message.get("config_file_creation_error"));
    }

    @Override
    public boolean fixVersions() {
        boolean fixed = false;
        for (ConfigChunk<?> configChunk : getConfigChunks()) {
            if (configChunk.isFixable()) {
                if (!doesChunkExist(configChunk)) {
                    config.set(configChunk.getKey(), config.get(configChunk.getOldKey(),configChunk.getDefaultValue()));
                    fixed = true;
                }
                if (config.get(configChunk.getOldKey(),null) != null) {
                    config.set(configChunk.getOldKey(), null);
                    fixed = true;
                }
            }
            setObjectFromChunk(configChunk);
        }

        if(fixed) plugin.saveConfig();
        return fixed;
    }

    @Override
    public <T> void setObjectFromChunk(ConfigChunk<T> chunk) {
        chunk.setValue(config.getObject(chunk.getKey(), chunk.getType(), chunk.getDefaultValue()));
    }

    @Override
    public <T> boolean doesChunkExist(ConfigChunk<T> chunk) {
        chunk.setValue(config.getObject(chunk.getKey(), chunk.getType(),null));
        return chunk.getValue() != null;
    }
}
