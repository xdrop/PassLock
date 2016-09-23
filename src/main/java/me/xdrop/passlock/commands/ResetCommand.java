package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameters;
import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.io.TextInputOutput;

import java.util.Arrays;

@Parameters(commandDescription = "Resets the datasource")
public class ResetCommand extends Command {

    public ResetCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    public ResetCommand(PasswordManager passwordManager, TextInputOutput tio) {
        super(passwordManager, tio);
    }

    @Override
    public void execute() throws CommandException {

        char[] secret;

        while(true) {

            tio.writeln("===========================");
            tio.writeln("Enter your secret password");
            tio.writeln("===========================");
            tio.writeln("");
            tio.writeln("--> The database gets locked with this password (!)");

            secret = tio.getSecure();
            tio.writeln("Enter again to confirm: ");
            char[] secret2 = tio.getSecure();

            if(Arrays.equals(secret, secret2)) {
                break;
            }

            tio.writeln("Passwords don't match try again");
            tio.writeln("");

        }

        passwordManager.initializeDatasource(secret);

        tio.writeln("Database initialized");

    }

}
