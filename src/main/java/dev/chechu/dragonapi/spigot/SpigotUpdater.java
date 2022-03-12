package dev.chechu.dragonapi.spigot;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.chechu.dragonapi.core.Core;
import dev.chechu.dragonapi.core.Updater;
import dev.chechu.dragonapi.core.utils.Message;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.logging.Logger;

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
    public void tryUpdate(boolean delete) {
        if(checkForNewVersion()) {
            if (this.isAutoUpdateEnabled()) {
                logger.info(Message.get("update_available_auto"));
                if(downloadNewVersion(delete)) logger.info(Message.get("update_success"));
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
        setCurrentMinor(Integer.parseInt(splittedVersion[1]));
        setCurrentPatch(Integer.parseInt(splittedVersion[2]));
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
            JsonElement element = JsonParser.parseReader(reader);
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
    public boolean downloadNewVersion(boolean delete) {
        File oldPlugin;
        File backup;
        File newPlugin;
        File oldFolder = new File(PLUGIN_FOLDER,"temp"+ (new Random().nextInt(255)));
        if(!oldFolder.exists())oldFolder.mkdir();
        PluginLoader loader = plugin.getPluginLoader();
        loader.disablePlugin(plugin);
        try {
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            oldPlugin = (File) getFileMethod.invoke(plugin);
            FileUtils.copyToDirectory(oldPlugin,oldFolder);
            backup = new File(oldFolder,oldPlugin.getName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            if (Core.debugMode)
                e.printStackTrace();
            return false;
        }
        newPlugin = downloadFile(DOWNLOAD_URL,PLUGIN_FOLDER,oldPlugin,delete);
        if(newPlugin == null) return false;

        try {
            Plugin thePlugin = loader.loadPlugin(newPlugin);
            String version = thePlugin.getDescription().getVersion();
            String name = thePlugin.getName();
            if(!newPlugin.renameTo(new File(newPlugin.getParentFile(),name+"-"+version+".jar"))) {
                if(Core.debugMode)
                    logger.severe(Message.get("update-filename-error"));
                if(!newPlugin.delete()) return false;
                FileUtils.copyFileToDirectory(backup,PLUGIN_FOLDER);
                return false;
            }
            loader.enablePlugin(thePlugin);
            FileUtils.deleteDirectory(oldFolder);
        } catch (InvalidPluginException | IOException e) {
            if(Core.debugMode)
                e.printStackTrace();
            return false;
        }
        return true;
    }
}