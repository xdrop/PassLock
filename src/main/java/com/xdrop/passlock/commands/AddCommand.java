package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.core.PasswordManagerAES;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.exceptions.AlreadyExistsException;
import com.xdrop.passlock.exceptions.CommandException;
import com.xdrop.passlock.io.TextInputOutput;
import com.xdrop.passlock.utils.ByteUtils;
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

    @Parameter(names = {"--pass", "-p"})
    private String newPassword;

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
            char[] newPassChar;

            if(newPassword == null){
                tio.write("Please enter the password you wish to store for " + ref + ": ");
                newPassChar = tio.getSecure();
            } else {
                newPassChar = newPassword.toCharArray();
            }

            passwordManager.addPassword("",
                    newPassChar,
                    masterKey, ref);

            tio.writeln("Successfully stored password [" + ref + "]");

        } catch (InvalidKeyException e) {

            throw new CommandException("Invalid master password.");

        } catch (AlreadyExistsException e) {

            throw new CommandException("The reference you have selected is not unique.");

        }


    }

}
