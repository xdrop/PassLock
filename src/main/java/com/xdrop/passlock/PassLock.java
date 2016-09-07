package com.xdrop.passlock;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.xdrop.passlock.commands.*;
import com.xdrop.passlock.exceptions.CommandException;

public class PassLock {



    public static void main(String[] args) {
        MainCommand cm = new MainCommand();
        JCommander jc = new JCommander(cm);

        AddCommand addCommand = new AddCommand();
        jc.addCommand("add", addCommand);
        DeleteCommand deleteCommand = new DeleteCommand();
        jc.addCommand("delete", addCommand);
        UpdateCommand updateCommand = new UpdateCommand();
        jc.addCommand("update", addCommand);
        GetCommand getCommand = new GetCommand();
        jc.addCommand("get", addCommand);

        jc.parse(args);
        String command = jc.getParsedCommand();

        try {
            if (command.equals("add")) {
                addCommand.execute();
            }
        } catch (CommandException ce){
            System.out.println(ce.getMessage());
        }


    }

}
