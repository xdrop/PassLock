package me.xdrop.passlock.settings;

public interface Settings {

    String getDbPath();

    int getNoOfSuggestions();

    int getRejectThreshold();

    String getAlgorithm();

    String userDir();

    String configFilePath();

    int getCertainMatchThreshold();

    boolean isSecureInput();
}
