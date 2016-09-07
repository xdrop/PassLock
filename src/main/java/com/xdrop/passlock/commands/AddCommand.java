package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.datasource.sqlite.SQLiteConnection;
import com.xdrop.passlock.exceptions.CommandException;

import java.util.List;

@Parameters(commandDescription = "Adds a new password")
public class AddCommand implements Command {

    @Parameter(names = {"--description", "--desc", "-d"})
    private String description;

    @Parameter(names = {"--pass", "-p"})
    private String newPassword;

    @Parameter(description = "Name/Reference to entry")
    private List<String> name;

    public void execute() throws CommandException {

        if(name.size() != 1){
            throw new CommandException("Invalid number of arguments.");
        }

        SQLiteConnection.connect();


        System.out.println(description+ " : " + newPassword
                            + " : " + name.get(0) );
    }
}
