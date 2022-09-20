package com.tobiassteely.eliteenchants.api.config;

import com.tobiassteely.eliteenchants.EliteEnchants;
import com.tobiassteely.eliteenchants.api.Log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigObject {

    private FileConfiguration fileConfiguration;
    private File file;
    private String configName;

    public ConfigObject(String configName) {
        this.configName = configName;
        this.file = new File(EliteEnchants.getInstance().getDataFolder(), configName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        this.fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (Exception ex) {
            Log.sendMessage(2, "Failed to load config (" + configName + ") please contact Tobias S., the plugin developer with the error below.");
            ex.printStackTrace();
        }

    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public int loadDefault(String key, Object object) {
        if (!getFileConfiguration().contains(key)) {
            getFileConfiguration().set(key, object);
            return 1;
        }
        return 0;
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException ex) {
            Log.sendMessage(2, "Failed to save config (" + configName + ") please contact Tobias S., the plugin developer with the error below.");
            ex.printStackTrace();
        }
    }

}
