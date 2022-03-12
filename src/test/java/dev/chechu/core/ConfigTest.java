package dev.chechu.core;

import dev.chechu.dragonapi.core.Configuration;
import dev.chechu.dragonapi.core.utils.ConfigChunk;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ConfigTest {
    @Test
    public void assertThatFileDoesntExist() {
        Configuration configuration = new Configuration() {
            @Override
            public void initializeConfig() {

            }

            @Override
            public void reloadConfig() {

            }

            @Override
            public void createFiles() {

            }

            @Override
            public boolean fixVersions() {
                return false;
            }

            @Override
            public <T> void setObjectFromChunk(ConfigChunk<T> chunk) {

            }

            @Override
            public <T> boolean doesChunkExist(ConfigChunk<T> chunk) {
                return false;
            }
        };
        configuration.checkFile(new File("a"));
    }
}
