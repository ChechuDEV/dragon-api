package dev.chechu.dragonapi.core.utils;

import lombok.*;

@Getter
public class ConfigChunk<T> {
    private final String key;
    private final T defaultValue;
    @Setter private T value;
    private final Class<T> type;
    private String oldKey = null;

    public ConfigChunk(String key, T defaultValue) {
        this.type = (Class<T>) defaultValue.getClass();
        this.defaultValue = defaultValue;
        this.key = key;
        this.value = defaultValue;
    }

    public ConfigChunk(String key, T defaultValue, String oldKey) {
        this.type = (Class<T>) defaultValue.getClass();
        this.defaultValue = defaultValue;
        this.key = key;
        this.value = defaultValue;
        this.oldKey = oldKey;
    }

    public boolean isFixable() {
        return oldKey != null;
    }
}
