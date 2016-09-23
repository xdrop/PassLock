package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.AlreadyExistsException;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.exceptions.RefNotFoundException;
import me.xdrop.passlock.io.TextInputOutput;

import java.util.List;

public class CopyCommand extends Command {

    @Parameter(names = {"--target", "-t"})
    private String targetName;

    @Parameter(description = "from to or simply from")
    private List<String> name;

    public CopyCommand(PasswordManager passwordManager, TextInputOutput tio) {
        super(passwordManager, tio);
    }

    public CopyCommand(PasswordManager passwordManager) {
        super(passwordManager);
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

        tio.write("Please enter your master password: ");

        char[] masterPassword = tio.getSecure();

        try{

            if ( !passwordManager.exists(ref) ) {
                throw new CommandException("Invalid reference, password doesn't exist");
            }

            if (!passwordManager.unlocksMaster(masterPassword)) {
                throw new CommandException("Invalid master password");
            }

            if(targetName == null){
                tio.write("Enter target name:");
                targetName = tio.getLine().trim();
            }

            passwordManager.copy(ref, targetName);
            tio.writeln("Password [" + ref + "] copied to -> [" + targetName + "]");

        } catch (RefNotFoundException e) {
            throw new CommandException("Source password could not be found.");
        } catch (AlreadyExistsException e) {
            throw new CommandException("Target password already exists.");
        }

    }
}
