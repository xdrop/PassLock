package me.xdrop.passlock.settings;

import me.xdrop.passlock.utils.GUtils;

import java.io.IOException;
import java.util.Properties;

public class SettingsFile extends DefaultSettings {

    private Properties config;

    public SettingsFile() {
        config = loadConfig();
    }

    public SettingsFile(String path) {
        config = loadConfig(path);
    }

    private Properties loadConfig(String path) {

        try {
            return GUtils.loadPropertiesFile(path);
        } catch (IOException e) {
            return null;
        }

    }

    private Properties loadConfig() {
        return loadConfig(super.configFilePath());
    }

    @Override
    public int getRejectThreshold() {
        return getOrDefaultInt("reject_threshold", super.getRejectThreshold());
    }

    @Override
    public int getCertainMatchThreshold() {
        return getOrDefaultInt("certain_match_threshold", super.getCertainMatchThreshold());
    }

    @Override
    public int getNoOfSuggestions() {
        return getOrDefaultInt("suggested", super.getNoOfSuggestions());
    }

    @Override
    public String getDbPath() {

        return getOrDefault("dbpath", super.getDbPath());

    }

    private String getOrDefault(String property, String defaultResult) {

        if (config != null) {

            String value = config.getProperty(property);

            if(value!=null) {
                return value;
            }

        }

        return defaultResult;

    }

    private int getOrDefaultInt(String property, int defaultResult) {

        if (config != null) {

            String value = config.getProperty(property);

            if(value!=null) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException ignored) {}
            }

        }

        return defaultResult;

    }


}
