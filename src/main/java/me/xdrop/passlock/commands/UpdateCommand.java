package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.exceptions.RefNotFoundException;
import me.xdrop.passlock.io.TextInputOutput;

import java.security.InvalidKeyException;
import java.util.List;

@Parameters(commandDescription = "Updates a password")
public class UpdateCommand extends Command {

    @Parameter(names = {"--pass", "-p"})
    private String newPassword;

    @Parameter(description = "Name/Reference to entry")
    private List<String> name;

    public UpdateCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    public UpdateCommand(PasswordManager passwordManager, TextInputOutput tio) {
        super(passwordManager, tio);
    }

    @Override
    public void execute() throws CommandException {

        if(name == null || name.size() != 1){
            throw new CommandException("Invalid number of arguments.");
        }

        String ref = name.get(0);

        tio.write("Please enter your master password: ");

        char[] masterPassword = tio.getSecure();
        char[] masterKey;
        char[] newPassC;

        try {

            if ( !passwordManager.exists(ref) ) {
                throw new CommandException("Invalid reference, password doesn't exist");
            }

            masterKey = passwordManager.getMasterKey(masterPassword);

            if(newPassword == null){
                tio.write("Please enter the new password you wish to store for [" + ref + "]: ");
                newPassC = tio.getSecure();
            } else{
                newPassC = newPassword.toCharArray();
            }

            passwordManager.updatePassword(ref,
                    masterKey, newPassC);

            tio.writeln("Successfully updated password for [" + ref + "]");

        } catch (InvalidKeyException e) {

            throw new CommandException("Invalid master password.");

        } catch (RefNotFoundException e) {
            throw new CommandException("Password doesn't exist");
        }

    }
}
