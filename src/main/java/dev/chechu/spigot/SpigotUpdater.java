package dev.chechu.spigot;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.chechu.core.Updater;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class SpigotUpdater extends Updater {
    private final String USER_AGENT  = "DragonAPI";// Change this!
    private final String REQUEST_URL;
    private final Logger logger;
    public SpigotUpdater(JavaPlugin plugin, String resourceID, boolean autoUpdate) {
        setAutoUpdateEnabled(autoUpdate);
        this.logger = plugin.getLogger();
        REQUEST_URL = "https://api.spiget.org/v2/resources/"+resourceID+"/versions/latest";
        setCurrentVersion(plugin.getDescription().getVersion());
        setLatestVersion(getLatestVersion());
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
        if (getCurrentMajor() < getLatestMajor()) {
            logger.warning("You're using an outdated version of the plugin!! The plugin will NOT try to update it, please update it manually.");
            return false;
        }
        if (getCurrentMinor() < getLatestMinor()) {
            if(this.isAutoUpdateEnabled()) {
                logger.info("A new update has been detected, the plugin will try to update it automatically");
            } else {
                logger.warning("A new update has been detected but you have disabled autoUpdates");
            }
        }
        return false;
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
            // TODO: handle exception
            e.printStackTrace();
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
    public void downloadNewVersion() {

    }
}