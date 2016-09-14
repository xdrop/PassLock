package com.xdrop.passlock.commands;

import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.exceptions.CommandException;

import java.util.List;

public class ListCommand extends Command {

    public ListCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    @Override
    public void execute() throws CommandException {

        tio.write("Please enter your master password: ");

        char[] masterPassword = tio.getSecure();

        if(!passwordManager.unlocksMaster(masterPassword)) {
            throw new CommandException("Invalid master password");
        }

        tio.writeln("===========");
        tio.writeln(" Passwords ");
        tio.writeln("===========");
        tio.writeln("");

        List<String> passList = passwordManager.list();

        for (int i = 0; i < passList.size(); i++) {

            tio.writeln("[" + (i+1) + "] " + passList.get(i));

        }

    }

}
