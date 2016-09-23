package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.exceptions.RefNotFoundException;
import me.xdrop.passlock.io.TextInputOutput;

import java.util.List;

public class RenameCommand extends Command {

    @Parameter(names = {"--target", "-t"})
    private String targetName;

    @Parameter(description = "Reference to rename")
    private List<String> name;

    @Parameter(names = {"-m"})
    private String masterPass;

    public RenameCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    public RenameCommand(PasswordManager passwordManager, TextInputOutput tio) {
        super(passwordManager, tio);
    }

    @Override
    public void execute() throws CommandException {

        if(name == null || name.size() < 1 || name.size() > 2){
            throw new CommandException("Invalid number of arguments.");
        }

        String ref;

        if (name.size() == 2) {
            ref = name.get(0);
            targetName = name.get(1);
        } else{
            ref = name.get(0);
        }

        char[] masterPassword;

        if(masterPass == null) {
            tio.write("Please enter your master password: ");
            masterPassword = tio.getSecure();
        } else{
            masterPassword = masterPass.toCharArray();
        }

        try{

            if ( !passwordManager.exists(ref) ) {
                throw new CommandException("Invalid reference, password doesn't exist");
            }

            if (!passwordManager.unlocksMaster(masterPassword)) {
                throw new CommandException("Invalid master password");
            }

            if(targetName == null){
                tio.write("Enter the new name:");
                targetName = tio.getLine().trim();
            }

            passwordManager.rename(ref, targetName);
            tio.writeln("Password [" + ref + "] renamed to -> [" + targetName + "]");

        } catch (RefNotFoundException e) {
            throw new CommandException("Source password could not be found.");
        }

    }

}
