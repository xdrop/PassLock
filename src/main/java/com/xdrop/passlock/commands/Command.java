package com.xdrop.passlock.commands;

import com.xdrop.passlock.exceptions.CommandException;

public interface Command {

    void execute() throws CommandException;

}
