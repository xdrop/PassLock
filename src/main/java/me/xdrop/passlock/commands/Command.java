package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.io.TextInputOutput;

public abstract class Command {

    protected TextInputOutput tio;
    protected PasswordManager passwordManager;

    @Parameter(names = {"-m"})
    private String masterPass;

    public Command(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
        this.tio = new TextInputOutput();
    }

    public Command(PasswordManager passwordManager, TextInputOutput tio) {
        this.passwordManager = passwordManager;
        this.tio = tio;
    }

    public abstract void execute() throws CommandException;

    protected char[] promptMasterPassword() {
        char[] masterPassword;

        if(masterPass == null) {
            tio.write("Please enter your master password: ");
            masterPassword = tio.getSecure();
        } else{
            masterPassword = masterPass.toCharArray();
        }
        return masterPassword;
    }
}
