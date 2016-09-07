package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameters;
import com.xdrop.passlock.exceptions.CommandException;

@Parameters(commandDescription = "Resets the datasource")
public class ResetCommand implements Command {

    @Override
    public void execute() throws CommandException {

    }

}
