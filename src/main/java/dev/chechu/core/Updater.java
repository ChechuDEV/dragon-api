package dev.chechu.core;

import lombok.*;

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

    public abstract void setCurrentVersion(String version);
    public abstract String getLatestVersion();
    public abstract void setLatestVersion(String version);

    public abstract boolean checkForNewVersion();

    public abstract void downloadNewVersion();


}
