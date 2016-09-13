package com.xdrop.passlock.commands;


import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.exceptions.CommandException;
import com.xdrop.passlock.io.TextInputOutput;

public abstract class Command {

    protected TextInputOutput tio;
    protected PasswordManager passwordManager;

    public Command(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
        this.tio = new TextInputOutput();
    }

    public abstract void execute() throws CommandException;

}
