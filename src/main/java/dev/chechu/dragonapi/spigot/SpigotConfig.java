package dev.chechu.dragonapi.spigot;

import dev.chechu.dragonapi.core.Configuration;
import dev.chechu.dragonapi.core.utils.ConfigChunk;
import dev.chechu.dragonapi.core.utils.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.util.logging.Logger;

public abstract class SpigotConfig extends Configuration<JavaPlugin> {
    @Getter(AccessLevel.PROTECTED) private FileConfiguration config;

    public SpigotConfig(Logger logger, JavaPlugin plugin) {
        super(logger, plugin);
        config = plugin.getConfig();
    }

    @Override
    public void createFiles() {
        if (!getPlugin().getDataFolder().exists()) if(!getPlugin().getDataFolder().mkdir()) {
            getLogger().severe(Message.get("config_folder_creation_error"));
        }
        getPlugin().saveDefaultConfig();
        File configFile = new File(getPlugin().getDataFolder() + "config.yml");
        if (!configFile.exists()) getLogger().severe(Message.get("config_file_creation_error"));
    }

    @Override
    public boolean fixVersions() {
        boolean backedUp = false;
        boolean fixed = false;
        for (ConfigChunk<?> configChunk : getConfigChunks()) {
            if (configChunk.isFixable()) {
                if(!backedUp) {
                    backupConfigFile();
                }
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

        if(fixed) getPlugin().saveConfig();
        return fixed;
    }

    @Override
    public void backupConfigFile() {
        FileUtil.copy(new File(getPlugin().getDataFolder(), "config.yml"), new File(getPlugin().getDataFolder(), "config.yml.backup"));
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

    @Override
    public void finishReloadConfig() {
        getPlugin().reloadConfig();
        config = getPlugin().getConfig();
    }
}
