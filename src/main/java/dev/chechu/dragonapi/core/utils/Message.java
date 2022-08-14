package dev.chechu.dragonapi.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Message {
    private static final HashMap<String, Map<String, Object>> apiLangs = new HashMap<>();
    private static final HashMap<String, Map<String, Object>> pluginLangs = new HashMap<>();
    private static final Yaml yaml = new Yaml();

    public Message() {
        for (File file : getFiles(getClass().getClassLoader())) {
            putTo(apiLangs, file);
        }
    }

    public Message(ClassLoader loader) {
        for (File file : getFiles(loader)) {
            putTo(pluginLangs, file);
        }
    }

    private void putTo(HashMap<String, Map<String, Object>> lang, File file) {
        try {
            InputStream in = new FileInputStream(file);
            Map<String, Object> data = yaml.load(in);
            lang.put(file.getName().replace(".yml", ""), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File[] getFiles(ClassLoader loader) {
        URL url = loader.getResource("./langs");
        assert url != null;
        String path = url.getPath();
        File[] files = new File(path).listFiles();
        assert files != null;
        return files;
    }

    public static String get(String lang, String key) {
        Map<String, Object> language = getLanguage(pluginLangs, lang);
        Object obj;
        if (language == null)
            obj = "404";
        else
            obj = language.getOrDefault(key, "404");

        if (obj.equals("404")) {
            language = getLanguage(apiLangs, lang);
            obj = language.getOrDefault(key, "LANGUAGE ERROR");
        }

        if (obj instanceof String)
            return (String) obj;
        return "LANGUAGE ERROR";
    }

    @Deprecated
    public static String get(String key) {
        return get("en_US", key);
    }

    private static Map<String, Object> getLanguage(HashMap<String, Map<String, Object>> locales, String lang) {
        if (locales.containsKey(lang)) {
            return locales.get(lang);
        } else {
            return locales.get("en_US");
        }
    }
}
