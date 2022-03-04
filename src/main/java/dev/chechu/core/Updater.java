package dev.chechu.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public abstract class Updater {
    @Getter(AccessLevel.PRIVATE) private String currentVersion;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private int currentMajor;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private int currentMinor;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private int currentPatch;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private int latestMajor;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private int latestMinor;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private int latestPatch;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private boolean autoUpdateEnabled = false;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private Logger logger;

    public abstract void tryUpdate(boolean delete);

    public abstract void setCurrentVersion(String version);
    public abstract String getLatestVersion();
    public abstract void setLatestVersion(String version);

    public abstract boolean checkForNewVersion();

    public abstract boolean downloadNewVersion(boolean delete);

    public File downloadFile(String urlString, File folder, File oldFile, boolean delete) {
        try {
            String filename = "temp.jar";

            File output = new File(folder, filename);
            FileUtils.copyURLToFile(new URL(urlString), output);
            /*if (!delete && oldFile != null) {
                if (!oldFile.renameTo(new File(oldFile.getParentFile(),oldFile.getName()+".old"))) {
                    output.delete();
                    return null;
                }
            } else */
            if (delete) {
                oldFile.delete();
            }
            return output;
        } catch (IOException e) {
            if (Core.debugMode)
                e.printStackTrace();
            return null;
        }
    }
}
