package dev.chechu.core;

import lombok.Getter;

import java.util.logging.Logger;

public class Core {
    @Getter private static Logger logger;
    public Core(Logger logger) {
        Core.logger = logger;

    }
}
