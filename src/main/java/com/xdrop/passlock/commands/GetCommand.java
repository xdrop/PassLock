package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

@Parameters(commandDescription = "Gets the password of this entry")
public class GetCommand implements Command {

    @Parameter(description = "Reference to the entry")
    private List<String> ref;

    public void execute() {

    }
}
