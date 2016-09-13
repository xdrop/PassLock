package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.core.PasswordManagerAES;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.exceptions.AlreadyExistsException;
import com.xdrop.passlock.exceptions.CommandException;
import com.xdrop.passlock.io.TextInputOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Parameters(commandDescription = "Adds a new password")
public class AddCommand extends Command {

    private final static Logger LOG = LoggerFactory.getLogger(AddCommand.class);

    @Parameter(names = {"--description", "--desc", "-d"})
    private String description;

    @Parameter(names = {"--pass", "-p"})
    private char[] newPassword;

    @Parameter(description = "Name/Reference to entry")
    private List<String> name;

    public AddCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    public void execute() throws CommandException {

        if(name == null || name.size() != 1){
            throw new CommandException("Invalid number of arguments.");
        }

        String ref = name.get(0);


        tio.write("Please enter your master password: ");

        char[] masterPassword = tio.getSecure();
        char[] masterKey;

        try {

            masterKey = passwordManager.getMasterKey(masterPassword);

            if(newPassword == null){
                tio.write("Please enter the password you wish to store for " + ref + ": ");
            }

            char[] newPassword = tio.getSecure();


            passwordManager.addPassword(description,
                    newPassword,
                    masterKey, name.get(0));

            tio.writeln("Successfully stored password [" + ref + "]");

        } catch (InvalidKeyException e) {

            throw new CommandException("Invalid master password.");

        } catch (AlreadyExistsException e) {

            throw new CommandException("The reference you have selected is not unique.");

        }


    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNewPassword(char[] newPassword) {
        this.newPassword = newPassword;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
