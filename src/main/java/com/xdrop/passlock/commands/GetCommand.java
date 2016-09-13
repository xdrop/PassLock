package com.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xdrop.passlock.core.PasswordManager;
import com.xdrop.passlock.core.PasswordManagerAES;
import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.crypto.aes.AESEncryptionModel;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.exceptions.CommandException;
import com.xdrop.passlock.exceptions.RefNotFoundException;
import com.xdrop.passlock.io.TextInputOutput;
import com.xdrop.passlock.search.DefaultSearch;
import com.xdrop.passlock.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parameters(commandDescription = "Gets the password of this entry")
public class GetCommand extends Command {

    private final static Logger LOG = LoggerFactory.getLogger(GetCommand.class);

    @Parameter(description = "Reference to the entry")
    private List<String> ref;

    public GetCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    public void execute() throws CommandException {

        if(ref == null || ref.size() != 1){
            throw new CommandException("Invalid number of arguments.");
        }

        String ref = this.ref.get(0);

        try {

            List<String> searchResults = passwordManager.search(new DefaultSearch(), ref, 5);

            if (searchResults.size() == 0) {
                throw new CommandException("Couldn't find password with reference: " + ref);
            }

            if (searchResults.size() > 1) {

                tio.writeln("Possible results found: ");
                tio.writeln("----------------------------");

                String choice = null;
                boolean selectionMade = false;

                while (!selectionMade) {

                    Map<String, String> choices = new HashMap<>();

                    for (int i = 1; i <= searchResults.size(); i++) {

                        String next = searchResults.get(i - 1);

                        choices.put(String.valueOf(i), next);
                        tio.writeln("[" + i + "] " + next);

                    }

                    tio.writeln("\nEnter choice:");
                    choice = choices.get(tio.getLine());

                    if(choice != null) {
                        selectionMade = true;
                    } else {
                        tio.writeln("Invalid choice.");
                        tio.writeln("");
                    }

                }

                ref = choice;

            } else {
                ref = searchResults.get(0);
            }
            tio.writeln("Showing password for [" + ref + "]");
            tio.writeln("Enter your master pass");

            char[] master = tio.getSecure();
            char[] masterKey = passwordManager.getMasterKey(master);
            byte[] pass = passwordManager.getPassword(ref, masterKey);

            tio.writeln("Your password is:");
            tio.writeSecureLn(pass);
            tio.writeln("");

        } catch (RefNotFoundException e) {

            throw new CommandException("Couldn't find password with reference: " + ref);

        } catch (InvalidKeyException e) {

            throw new CommandException("Invalid master password");

        }


    }

    public void setRef(List<String> ref) {
        this.ref = ref;
    }
}
