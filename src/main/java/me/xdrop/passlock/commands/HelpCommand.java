package me.xdrop.passlock.commands;

import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.CommandException;

public class HelpCommand extends Command {

    public HelpCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    @Override
    public void execute() throws CommandException {
        tio.writeln("PassLock 1.0.13");
        tio.writeln("Copyright (C) 2015 Free Software Foundation, Inc.");
        tio.writeln("License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>");
        tio.writeln("This is free software: you are free to change and redistribute it.");
        tio.writeln("There is NO WARRANTY, to the extent permitted by law.");
        tio.writeln("");
        tio.writeln("Usage: plock [--config <path>] [--db <path>] [command] [parameters]");
        tio.writeln("");
        tio.writeln("Available commands:");
        tio.writeln("");
        tio.writeln("g,  get    <password to search for>                 :: Get a password ");
        tio.writeln("d,  delete <password to delete>                     :: Delete a password");
        tio.writeln("a,  add    [-p <newpass>] <identifier for password> :: Adds a new password");
        tio.writeln("u,  update [-p <newpass>] <target ref>              :: Updates a password");
        tio.writeln("cp, copy   [-t <target>]  <source ref>              :: Copies source to target");
        tio.writeln("mv, r      [-t <target>]  <source ref>              :: Renames source to target");
        tio.writeln("ls                                                  :: Lists all passwords");
        tio.writeln("reset                                               :: Resets the datasource");
        tio.writeln("");
        tio.writeln("* Ref indicates a reference/identifer for a password");
        tio.writeln("");
        tio.writeln("Examples:");
        tio.writeln("");
        tio.writeln("plock g www.google.com                          :: Get password for google");
        tio.writeln("plock a www.google.com                          :: Add password for google.com");
        tio.writeln("plock cp www.google.com www.gmail.com           :: Copy www.google.com to www.gmail.com");
        tio.writeln("plock cp www.google.com -t www.gmail.com        :: Copy www.google.com to www.gmail.co");
        tio.writeln("");
        tio.writeln("Please report any bugs to http://github.com/xdrop/passlock/issues");
        tio.writeln("");
        tio.writeln("");
    }

}
