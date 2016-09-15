package me.xdrop.passlock.settings;

public enum SettingsProvider {
    INSTANCE;

    private Settings settings;

    SettingsProvider() {
        settings = new SettingsFile();
    }

    public Settings getSettings() {
        return settings;
    }
}
