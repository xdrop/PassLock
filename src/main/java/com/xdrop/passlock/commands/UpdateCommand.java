package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.exceptions.CommandException;

@Parameters(commandDescription = "Updates a password")
public class UpdateCommand extends Command {

    public UpdateCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    @Parameter(names = {"--description", "--desc", "-d"})
    private String description;

    @Parameter(names = {"--pass", "-p"})
    private String newPassword;

    @Parameter(description = "Name/Reference to entry")
    private String name;


    @Override
    public void execute() throws CommandException {

    }
}
