package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.exceptions.AlreadyExistsException;
import com.xdrop.passlock.exceptions.CommandException;
import com.xdrop.passlock.exceptions.RefNotFoundException;

import java.security.InvalidKeyException;
import java.util.List;

@Parameters(commandDescription = "Updates a password")
public class UpdateCommand extends Command {

    public UpdateCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    @Parameter(names = {"--pass", "-p"})
    private char[] newPassword;

    @Parameter(description = "Name/Reference to entry")
    private List<String> name;


    @Override
    public void execute() throws CommandException {

        if(name == null || name.size() != 1){
            throw new CommandException("Invalid number of arguments.");
        }

        String ref = name.get(0);


        tio.write("Please enter your master password: ");

        char[] masterPassword = tio.getSecure();
        char[] masterKey;

        try {

            if ( !passwordManager.exists(ref) ) {
                throw new CommandException("Invalid reference, password doesn't exist");
            }

            masterKey = passwordManager.getMasterKey(masterPassword);

            if(newPassword == null){
                tio.write("Please enter the new password you wish to store for [" + ref + "]: ");
                newPassword = tio.getSecure();
            }

            passwordManager.updatePassword(ref,
                    masterKey, newPassword);

            tio.writeln("Successfully updated password for [" + ref + "]");

        } catch (InvalidKeyException e) {

            throw new CommandException("Invalid master password.");

        } catch (RefNotFoundException e) {
            throw new CommandException("Password doesn't exist");
        }


    }
}
