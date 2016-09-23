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
        String datasourcePath;

        Settings settings = SettingsProvider.INSTANCE.getSettings();

        PasswordManager passwordManager = new PasswordManagerAES(null);

        TextInputOutput tio = new TextInputOutput();
        tio.setSecure(settings.isSecureInput());

        MainCommand cm = new MainCommand();
        JCommander jc = new JCommander(cm);

        Map<String, Command> commands = new HashMap<>();

        bindCommands(tio, passwordManager, jc, commands);

        String command;

        try {
            jc.parse(args);
            command = jc.getParsedCommand();
        } catch (Exception e) {
            tio.writeln("Invalid command");
            return;
        }

        if(cm.getConfigFile() != null) {
            SettingsProvider.INSTANCE.loadFile(cm.getConfigFile());
        }

        /* we cheated by passing a null reference, now depending on if an argument was passed
         * we create the datasource accordingly */
        if (cm.getDbPath() != null) {
            datasourcePath = GUtils.resolvePath(cm.getDbPath());
        } else{
            datasourcePath = GUtils.resolvePath(settings.getDbPath());
        }

        if (cm.isSecureInput() != null){
            tio.setSecure(cm.isSecureInput());
        }

        GUtils.createIfDoesntExist(datasourcePath);
        passwordManager.setDatasource(new SQLiteAESDatasource(datasourcePath));

        /* if the datasource isn't prepared, prepare it before doing anything else */
        if (!passwordManager.isInitialized()) {

            try {
                tio.writeln("This is your first time running, initializing database");
                new ResetCommand(passwordManager, tio).execute();

                if (command != null && commands.get(command) instanceof ResetCommand) {
                    return;
                }
            } catch (CommandException ignored) {
            }

        }

        if (command == null) {
            tio.writeln("Invalid command, exiting.");

            try {
                new HelpCommand(null).execute();
            } catch (CommandException ignored) {}

            return;
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

    private void bindCommands(TextInputOutput tio, PasswordManager passwordManager, JCommander jc, Map<String, Command> commands) {

        AddCommand addCommand = new AddCommand(passwordManager, tio);
        registerCommand(jc, addCommand, commands, "add", "a");

        DeleteCommand deleteCommand = new DeleteCommand(passwordManager, tio);
        registerCommand(jc, deleteCommand, commands, "delete", "rm", "d");

        UpdateCommand updateCommand = new UpdateCommand(passwordManager, tio);
        registerCommand(jc, updateCommand, commands, "update", "u");

        GetCommand getCommand = new GetCommand(passwordManager, tio);
        registerCommand(jc, getCommand, commands, "get", "g");

        ResetCommand resetCommand = new ResetCommand(passwordManager, tio);
        registerCommand(jc, resetCommand, commands, "reset", "rst");

        ListCommand listCommand = new ListCommand(passwordManager, tio);
        registerCommand(jc, listCommand, commands, "list", "l", "ls");

        RenameCommand renameCommand = new RenameCommand(passwordManager, tio);
        registerCommand(jc, renameCommand, commands, "rename", "mv", "r");

        CopyCommand copyCommand = new CopyCommand(passwordManager, tio);
        registerCommand(jc, copyCommand, commands, "copy", "cp", "c");

        HelpCommand helpCommand = new HelpCommand(passwordManager, tio);
        registerCommand(jc, helpCommand, commands, "--help", "-h");

    }

}
