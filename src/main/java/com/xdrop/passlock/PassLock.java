package com.xdrop.passlock;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.xdrop.passlock.commands.*;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.core.PasswordManagerAES;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.exceptions.CommandException;
import com.xdrop.passlock.io.TextInputOutput;
import com.xdrop.passlock.settings.DefaultSettings;
import com.xdrop.passlock.settings.Settings;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PassLock {

    public static Properties loadPropertiesFile(String filename) {

        Properties properties = new Properties();
        InputStream in;

        in = PassLock.class.getClassLoader().getResourceAsStream(filename);

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;

    }

    public static Settings getSettings() {

        return new DefaultSettings();

    }

    public static void main(String[] args) {

        PropertyConfigurator.configure(loadPropertiesFile("log.properties"));

        TextInputOutput tio = new TextInputOutput();
        PasswordManager passwordManager = new PasswordManagerAES(new SQLiteAESDatasource());

        if(!passwordManager.isInitialized()){
            try {
                tio.writeln("This is your first time running, initializing database");
                new ResetCommand(passwordManager).execute();
            } catch (CommandException ignored) {}
        }

        MainCommand cm = new MainCommand();
        JCommander jc = new JCommander(cm);

        Map<String, Command> commands = new HashMap<>();

        AddCommand addCommand = new AddCommand(passwordManager);
        jc.addCommand("add", addCommand);
        jc.addCommand("a", addCommand);
        commands.put("add", addCommand);

        DeleteCommand deleteCommand = new DeleteCommand(passwordManager);
        jc.addCommand("delete", deleteCommand);
        jc.addCommand("d", deleteCommand);
        commands.put("delete", deleteCommand);

        UpdateCommand updateCommand = new UpdateCommand(passwordManager);
        jc.addCommand("update", updateCommand);
        jc.addCommand("u", updateCommand);
        commands.put("update", updateCommand);

        GetCommand getCommand = new GetCommand(passwordManager);
        jc.addCommand("get", getCommand);
        jc.addCommand("g", getCommand);
        commands.put("get", getCommand);

        ResetCommand resetCommand = new ResetCommand(passwordManager);
        jc.addCommand("reset", resetCommand);
        commands.put("reset", resetCommand);


        jc.parse(args);
        String command = jc.getParsedCommand();

        if(command == null) {
            tio.writeln("Invalid command, exiting.");
            return;
        }

        try {

            Command cmd = commands.get(command);
            if(cmd == null) {
                tio.writeln("Command not found, exiting.");
                return;
            }
            cmd.execute();

        } catch (CommandException ce) {
            tio.writeln(ce.getMessage());
        }


    }

}
