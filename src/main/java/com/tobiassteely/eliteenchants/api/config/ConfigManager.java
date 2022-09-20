package com.tobiassteely.eliteenchants.api.config;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigManager {

    private ArrayList<ConfigObject> configObjects = new ArrayList<>();
    private HashMap<String, ConfigObject> configNameCache = new HashMap<>(); // Key : Config Name, Value : Config Object

    public ConfigObject getConfig(String configName) {
        if(!configNameCache.containsKey(configName))
            loadConfig(configName);
        return configNameCache.get(configName);
    }

    public boolean loadConfig(String configName) {
        if (!configNameCache.containsKey(configName)) {
            ConfigObject configObject = new ConfigObject(configName);
            configObjects.add(configObject);
            configNameCache.put(configName, configObject);
            return true;
        }
        return false;
    }

    public HashMap<String, ConfigObject> getConfigNameCache() {
        return configNameCache;
    }
}
