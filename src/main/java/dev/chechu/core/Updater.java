package dev.chechu.core;

import lombok.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
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

    public abstract void tryUpdate();

    public abstract void setCurrentVersion(String version);
    public abstract String getLatestVersion();
    public abstract void setLatestVersion(String version);

    public abstract boolean checkForNewVersion();

    public abstract boolean downloadNewVersion();

    public boolean downloadFile(String urlString, File file, File oldFile) {
        try {
            URL url = new URL(urlString);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.getChannel().transferFrom(readableByteChannel,0,Long.MAX_VALUE);
        } catch (IOException e) {
            if (Core.debugMode)
                e.printStackTrace();
            return false;
        }
        if (oldFile != null) {
            if (!oldFile.renameTo(new File(oldFile.getParentFile(),oldFile.getName()+".old"))) {
                file.delete();
                return false;
            }
        }
        return true;
    }
}
