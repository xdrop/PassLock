package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.core.PasswordManagerAES;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.exceptions.CommandException;

import java.io.Console;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.util.List;

@Parameters(commandDescription = "Adds a new password")
public class AddCommand implements Command {

    @Parameter(names = {"--description", "--desc", "-d"})
    private String description;

    @Parameter(names = {"--pass", "-p"})
    private char[] newPassword;

    @Parameter(description = "Name/Reference to entry")
    private List<String> name;

    private Console console;
    private PrintWriter pw;

    public AddCommand(OutputStream out) {
        this.console = System.console();
        this.pw = new PrintWriter(out);
    }

    public void execute() throws CommandException {

        if(name.size() != 1 || name == null){
            throw new CommandException("Invalid number of arguments.");
        }

        String ref = name.get(0);

        PasswordManager passwordManager = new PasswordManagerAES(new SQLiteAESDatasource());

        pw.write("Please enter your master password:\n");
        pw.flush();

        char[] masterPassword = console.readPassword();
        char[] masterKey;

        try {

            masterKey = passwordManager.getMasterKey(masterPassword);

        } catch (InvalidKeyException e) {

            pw.write("Invalid master password. Exiting");
            return;

        }

        if(newPassword == null){
            pw.write("Please enter the password you wish to store for " + ref + " :\n");
            pw.flush();
        }

        char[] newPassword = console.readPassword();

//        passwordManager.addPassword(description,
//                newPassword,
//                masterKey, name.get(0));

    }
}
