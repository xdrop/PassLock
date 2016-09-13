package com.xdrop.passlock.settings;

public class DefaultSettings implements Settings {

    @Override
    public int getNoOfSuggestions() {
        return 5;
    }

    @Override
    public int getThreshold() {
        return 70;
    }

    @Override
    public String getAlgorithm() {
        return "AES";
    }

}
