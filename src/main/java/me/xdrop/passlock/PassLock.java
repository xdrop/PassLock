package me.xdrop.passlock;

import com.beust.jcommander.JCommander;
import me.xdrop.passlock.commands.*;
import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.core.PasswordManagerAES;
import me.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.io.TextInputOutput;
import me.xdrop.passlock.settings.Settings;
import me.xdrop.passlock.settings.SettingsProvider;
import me.xdrop.passlock.utils.GUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PassLock {

    private final static Logger LOG = LoggerFactory.getLogger(PassLock.class);

    public static Properties loadResourceProperties(String filename) {

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


    public static void main(String[] args) {

        PassLock passLock = new PassLock();
        passLock.init(args);

    }

    private void registerCommand(JCommander jc, Command command, Map<String, Command> commands, String... args) {

        for (String s : args) {

            jc.addCommand(s, command);
            commands.put(s, command);
        }

    }

    private void init(String[] args) {

        PropertyConfigurator.configure(loadResourceProperties("log.properties"));

        TextInputOutput tio = new TextInputOutput();
        Settings settings = SettingsProvider.INSTANCE.getSettings();

        GUtils.createIfDoesntExist(settings.getDbPath());

        PasswordManager passwordManager =
                new PasswordManagerAES(null);


        MainCommand cm = new MainCommand();
        JCommander jc = new JCommander(cm);

        Map<String, Command> commands = new HashMap<>();

        AddCommand addCommand = new AddCommand(passwordManager);
        registerCommand(jc, addCommand, commands, "add", "a");

        DeleteCommand deleteCommand = new DeleteCommand(passwordManager);
        registerCommand(jc, deleteCommand, commands, "delete", "d", "del");

        UpdateCommand updateCommand = new UpdateCommand(passwordManager);
        registerCommand(jc, updateCommand, commands, "update", "u");

        GetCommand getCommand = new GetCommand(passwordManager);
        registerCommand(jc, getCommand, commands, "get", "g");

        ResetCommand resetCommand = new ResetCommand(passwordManager);
        registerCommand(jc, resetCommand, commands, "reset", "rst");

        ListCommand listCommand = new ListCommand(passwordManager);
        registerCommand(jc, listCommand, commands, "list", "l", "ls");

        RenameCommand renameCommand = new RenameCommand(passwordManager);
        registerCommand(jc, renameCommand, commands, "rename", "mv", "r");

        CopyCommand copyCommand = new CopyCommand(passwordManager);
        registerCommand(jc, copyCommand, commands, "copy", "cp", "c");

        String command;

        try {
            jc.parse(args);
            command = jc.getParsedCommand();
        } catch (Exception e) {
            tio.writeln("Invalid command");
            return;
        }

        // we cheated by passing a null reference, now depending on if an argument was passed
        // we create the datasource accordingly
        if (cm.getDbPath() != null) {
            passwordManager.setDatasource(new SQLiteAESDatasource(cm.getDbPath()));
        } else{
            passwordManager.setDatasource(new SQLiteAESDatasource(settings.getDbPath()));
        }

        if(cm.getConfigFile() != null) {
            SettingsProvider.INSTANCE.loadFile(cm.getConfigFile());
        }

        if (command == null) {
            tio.writeln("Invalid command, exiting.");
            return;
        }

        if (!passwordManager.isInitialized()) {

            try {
                tio.writeln("This is your first time running, initializing database");
                new ResetCommand(passwordManager).execute();

                if (commands.get(command) instanceof ResetCommand) {
                    return;
                }
            } catch (CommandException ignored) {
            }

        }

        try {

            Command cmd = commands.get(command);
            if (cmd == null) {
                tio.writeln("Command not found, exiting.");
                return;
            }
            cmd.execute();

        } catch (CommandException ce) {
            tio.writeln(ce.getMessage());
        }

    }

}
