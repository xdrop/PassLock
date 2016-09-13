package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.exceptions.CommandException;

import java.util.List;

@Parameters(commandDescription = "Deletes a password")
public class DeleteCommand implements Command {

    @Parameter(description = "Reference to delete")
    private List<String> ref;


    @Override
    public void execute() throws CommandException {

    }
}
