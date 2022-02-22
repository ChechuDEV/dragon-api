package dev.chechu.core.utils;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Sender {
    @Getter private boolean consoleSender;
    @Getter private boolean commandBlockSender;
    @Getter private boolean playerSender;
    @Getter private Object sender;

    /**
     * Sends message to the Sender
     * @param message Message to be sent
     */
    public abstract void sendMessage(@NotNull String message);

    /**
     * Sends actionBar to the Sender
     * @param actionBar Action bar to be displayed
     */
    public abstract void sendActionBar(@NotNull String actionBar);

    /**
     * Sends a title to the Sender
     * @param title Title to be shown
     * @param subtitle Subtitle to be shown
     */
    public abstract void sendTitle(@NotNull String title, @Nullable String subtitle);
}
