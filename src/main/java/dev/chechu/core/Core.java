package dev.chechu.core;

import dev.chechu.core.utils.Message;

public class Core {
    public static boolean debugMode = false;
    public Core(boolean debugMode) {
        new Message();
        Core.debugMode = debugMode;
    }
}
