package dev.chechu.spigot;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.chechu.core.Core;
import dev.chechu.core.Updater;
import dev.chechu.core.utils.Message;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

public class SpigotUpdater extends Updater {
    private final String USER_AGENT  = "DragonAPI";// Change this!
    private final String REQUEST_URL;
    private final String DOWNLOAD_URL;
    private final File PLUGIN_FOLDER;
    private final Logger logger;
    private final JavaPlugin plugin;
    public SpigotUpdater(JavaPlugin plugin, String resourceID, boolean autoUpdate) {
        this.plugin = plugin;
        setAutoUpdateEnabled(autoUpdate);
        this.logger = plugin.getLogger();
        REQUEST_URL = "https://api.spiget.org/v2/resources/"+resourceID+"/versions/latest";
        DOWNLOAD_URL = "https://api.spiget.org/v2/resources/"+resourceID+"/download";
        PLUGIN_FOLDER = plugin.getDataFolder().getParentFile();
        setCurrentVersion(plugin.getDescription().getVersion());
    }

    @Override
    public void tryUpdate() {
        if(checkForNewVersion()) {
            if (this.isAutoUpdateEnabled()) {
                logger.info(Message.get("update_available_auto"));
                if(downloadNewVersion()) logger.info(Message.get("update_success"));
                else logger.info(Message.get("update_fail"));
            } else {
                logger.warning(Message.get("update_available_no_auto"));
            }
        }
    }

    @Override
    public void setCurrentVersion(String version) {
        String[] splittedVersion = version.split("[.]");
        if(splittedVersion.length < 3) return;
        setCurrentMajor(Integer.parseInt(splittedVersion[0]));
        setCurrentMajor(Integer.parseInt(splittedVersion[1]));
        setCurrentMajor(Integer.parseInt(splittedVersion[2]));
    }

    @Override
    public boolean checkForNewVersion() {
        logger.info(Message.get("update_search"));
        setLatestVersion(getLatestVersion());
        if (getCurrentMajor() < getLatestMajor()) {
            logger.warning(Message.get("update_major_difference"));
            return false;
        }
        return getCurrentMinor() < getLatestMinor() || getCurrentPatch() < getLatestPatch();
    }

    @Override
    public String getLatestVersion() {
        try {
            URL url = new URL(REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);// Set User-Agent

            // If you're not sure if the request will be successful,
            // you need to check the response code and use #getErrorStream if it returned an error code
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            // This could be either a JsonArray or JsonObject
            JsonElement element = new JsonParser().parse(reader);
            if (element.isJsonObject()) {
                return element.getAsJsonObject().get("name").getAsString();
            }
        } catch (IOException e) {
            logger.severe(Message.get("update_version_fail"));
            if (Core.debugMode) {
                e.printStackTrace();
            }
        }
        return "-1.-1.-1";
    }

    @Override
    public void setLatestVersion(String version) {
        String[] splittedVersion = version.split("[.]");
        if(splittedVersion.length < 3) return;
        setLatestMajor(Integer.parseInt(splittedVersion[0]));
        setLatestMinor(Integer.parseInt(splittedVersion[1]));
        setLatestPatch(Integer.parseInt(splittedVersion[2]));
    }

    @Override
    public boolean downloadNewVersion() {
        File oldPlugin;
        File newPlugin;
        PluginLoader loader = plugin.getPluginLoader();
        loader.disablePlugin(plugin);
        try {
            newPlugin = new File(PLUGIN_FOLDER, FilenameUtils.getName((new URL(DOWNLOAD_URL)).getPath()));
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            oldPlugin = (File) getFileMethod.invoke(plugin);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | MalformedURLException e) {
            if (Core.debugMode)
                e.printStackTrace();
            return false;
        }

        if(!downloadFile(DOWNLOAD_URL,newPlugin,oldPlugin)) return false;

        try {
            Plugin thePlugin = loader.loadPlugin(newPlugin);
            loader.enablePlugin(thePlugin);
        } catch (InvalidPluginException e) {
            if(Core.debugMode)
                e.printStackTrace();
        }


        return true;
    }
}