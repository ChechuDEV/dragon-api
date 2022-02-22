package dev.chechu.core;

import java.io.File;

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
    void fixVersions();

    /**
     * Checks if a file exists
     * @param file File to check
     * @return Whether the file exists
     */
    default boolean checkFile(File file) {
        return file.exists();
    }

}
