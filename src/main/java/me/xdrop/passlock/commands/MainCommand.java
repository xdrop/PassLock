package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;

public class MainCommand {

    @Parameter(names = {"--db", "-f"})
    private String dbpath;

    public String getDbPath() {
        return dbpath;
    }
}
