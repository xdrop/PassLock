package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

@Parameters(commandDescription = "Deletes a password")
public class DeleteCommand {

    @Parameter(description = "Reference to delete")
    private List<String> ref;


}
