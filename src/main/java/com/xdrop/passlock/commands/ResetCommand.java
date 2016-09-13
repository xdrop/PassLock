package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameters;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.core.PasswordManagerAES;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.exceptions.CommandException;
import com.xdrop.passlock.io.TextInputOutput;

import java.util.Arrays;

@Parameters(commandDescription = "Resets the datasource")
public class ResetCommand implements Command {

    private TextInputOutput tio;

    public ResetCommand() {
        this.tio = new TextInputOutput();
    }

    @Override
    public void execute() throws CommandException {

        PasswordManager passwordManager = new PasswordManagerAES(new SQLiteAESDatasource());

        char[] secret = null;

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
