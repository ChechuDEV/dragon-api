package dev.chechu.core;

import dev.chechu.core.utils.ConfigChunk;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Configuration {
    @Getter(AccessLevel.PROTECTED) private final List<ConfigChunk<?>> configChunks = new ArrayList<>();
    @Getter @Setter(AccessLevel.PROTECTED) private Logger logger;

    /**
     * Initializes configuration, by checking, creating and fixing files and lastly reloading the config.
     */
    public abstract void initializeConfig();

    /**
     * Reload all the config
     */
    public abstract void reloadConfig();

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
     * @param <T> Type of chunk.
     */
    public abstract <T> void setObjectFromChunk(ConfigChunk<T> chunk);

    public abstract <T> boolean doesChunkExist(ConfigChunk<T> chunk);

    /**
     * Checks if a file exists
     * @param file File to check
     * @return Whether the file exists
     */
    public boolean checkFile(File file) {
        return file.exists();
    }
}
