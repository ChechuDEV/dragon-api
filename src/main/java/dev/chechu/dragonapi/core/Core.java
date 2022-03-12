package dev.chechu.dragonapi.core;

import dev.chechu.dragonapi.core.utils.Message;

public class Core {
    public static boolean debugMode = false;
    public Core(boolean debugMode) {
        new Message();
        Core.debugMode = debugMode;
    }
}
