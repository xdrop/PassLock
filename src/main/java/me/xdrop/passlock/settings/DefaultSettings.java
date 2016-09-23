package me.xdrop.passlock.settings;

import java.io.File;

public abstract class DefaultSettings implements Settings {

    @Override
    public String getDbPath(){
        return userDir() + File.separator + "store.db";
    }

    @Override
    public int getNoOfSuggestions() {
        return 5;
    }

    @Override
    public int getRejectThreshold() {
        return 40;
    }

    @Override
    public String getAlgorithm() {
        return "AES";
    }

    @Override
    public String userDir() {
        return System.getProperty("user.home") + File.separator + ".passlock";
    }

    @Override
    public String configFilePath() {
        return userDir() + File.separator + "config";
    }

    @Override
    public int getCertainMatchThreshold(){
        return 94;
    }

    @Override
    public boolean isSecureInput() { return true; }
}
