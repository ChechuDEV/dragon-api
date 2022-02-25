package dev.chechu.core;

import dev.chechu.core.utils.ConfigChunk;

import java.io.File;
import java.util.List;

public interface Configuration {

    /**
     * Initializes configuration, by checking, creating and fixing files and lastly reloading the config.
     */
    void initializeConfig();

    /**
     * Reload all the config
     */
    void reloadConfig();

    //TODO: Parameters
    /**
     * Creates a specified file
     */
    void createFiles();

    /**
     * Fixes configuration versions.
     */
    boolean fixVersions();

    /**
     * Sets the object to it value or default value.
     * @param chunk Chunk from which to extract the object, key and default value.
     * @param <T> Type of chunk.
     */
    <T> void setObjectFromChunk(ConfigChunk<T> chunk);

    <T> boolean doesChunkExist(ConfigChunk<T> chunk);

    /**
     * Checks if a file exists
     * @param file File to check
     * @return Whether the file exists
     */
    default boolean checkFile(File file) {
        return file.exists();
    }
}
