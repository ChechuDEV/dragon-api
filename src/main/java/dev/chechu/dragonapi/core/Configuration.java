package dev.chechu.dragonapi.core;

import dev.chechu.dragonapi.core.utils.ConfigChunk;
import dev.chechu.dragonapi.core.utils.Message;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Configuration<T> {
    @Getter(AccessLevel.PROTECTED) private final List<ConfigChunk<?>> configChunks = new ArrayList<>();
    @Getter private final Logger logger;
    @Getter private final T plugin;

    private static Configuration<?> instance;
    public Configuration(Logger logger, T plugin) {

        this.logger = logger;
        this.plugin = plugin;
        createFiles();
        initializeConfig();
        setConfigChunks();
        if(fixVersions()) getLogger().info(Message.get("config_fix"));
    }

    public static <T> Configuration<T> getInstance(Class<T> clazz) {
        if(instance != null && instance.getClass().isInstance(clazz))
            return (Configuration<T>) instance;
        throw new RuntimeException("Instance class doesn't match the requested one");
    }

    /**
     * Initializes configuration, by checking, creating and fixing files and lastly reloading the config.
     */
    public abstract void initializeConfig();

    /**
     * Reload all the config
     */
    public void reloadConfig() {
        extraReloadConfig();
        finishReloadConfig();
        setConfigChunks();
    }

    public abstract void finishReloadConfig();

    public abstract void extraReloadConfig();

    //TODO: Parameters
    /**
     * Creates a specified file
     */
    public abstract void createFiles();

    /**
     * Fixes configuration versions.
     */
    public abstract boolean fixVersions();

    /**
     * Sets the object to it value or default value.
     * @param chunk Chunk from which to extract the object, key and default value.
     * @param <K> Type of chunk.
     */
    public abstract <K> void setObjectFromChunk(ConfigChunk<K> chunk);

    public abstract <K> boolean doesChunkExist(ConfigChunk<K> chunk);

    /**
     * Checks if a file exists
     * @param file File to check
     * @return Whether the file exists
     */
    public boolean checkFile(File file) {
        return file.exists();
    }

    public void setConfigChunks() {
        for (ConfigChunk<?> configChunk : configChunks) {
            setObjectFromChunk(configChunk);
        }
    }

    public <K> void addToConfigChunks(ConfigChunk<K> chunk) {
        configChunks.add(chunk);
    }

    public abstract void backupConfigFile();
}
