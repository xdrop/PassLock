package me.xdrop.passlock.commands;


import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.io.TextInputOutput;

public abstract class Command {

    protected TextInputOutput tio;
    protected PasswordManager passwordManager;

    public Command(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
        this.tio = new TextInputOutput();
    }

    public abstract void execute() throws CommandException;

}
