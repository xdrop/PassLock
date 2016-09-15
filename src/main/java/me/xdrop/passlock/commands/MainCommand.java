package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

public class MainCommand {

    @Parameter(names = {"--db", "-f"})
    private String dbpath;

    @Parameter(names = {"--conf", "-c"})
    private String configFile;

    public String getDbPath() {
        return dbpath;
    }

    public String getConfigFile() {
        return configFile;
    }
}
