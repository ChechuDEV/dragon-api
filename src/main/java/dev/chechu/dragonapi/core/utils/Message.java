package dev.chechu.dragonapi.core.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Message {
    private static Map<String, Object> data;
    public Message() {
        Yaml yaml = new Yaml();
        try {
            InputStream in = getClass().getResourceAsStream("/langs/en_US.yml");
            data = yaml.load(in);
        } catch (Exception e) {
            e.printStackTrace();
            data = new HashMap<>();
        }
    }
    public static String get(String key) {
        Object obj = data.getOrDefault(key,"LANGUAGE ERROR");
        if (obj instanceof String)
            return (String) obj;
        return "LANGUAGE ERROR";
    }
}
