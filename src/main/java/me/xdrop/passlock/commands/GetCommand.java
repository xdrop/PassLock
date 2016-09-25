package me.xdrop.passlock.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import me.xdrop.passlock.core.PasswordManager;
import me.xdrop.passlock.exceptions.CommandException;
import me.xdrop.passlock.exceptions.RefNotFoundException;
import me.xdrop.passlock.io.TextInputOutput;
import me.xdrop.passlock.search.DefaultSearch;

import me.xdrop.passlock.search.FuzzySearcher;
import me.xdrop.passlock.settings.Settings;
import me.xdrop.passlock.settings.SettingsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parameters(commandDescription = "Gets the password of this entry")
public class GetCommand extends Command {

    private final static Logger LOG = LoggerFactory.getLogger(GetCommand.class);

    @Parameter(description = "Reference to the entry")
    private List<String> names;

    public GetCommand(PasswordManager passwordManager) {
        super(passwordManager);
    }

    public GetCommand(PasswordManager passwordManager, TextInputOutput tio) {
        super(passwordManager, tio);
    }

    public void execute() throws CommandException {

        if(names == null){
            throw new CommandException("Invalid number of arguments.");
        }

        String ref;
        Settings settings = SettingsProvider.INSTANCE.getSettings();

        FuzzySearcher fuzzySearcher = new DefaultSearch(settings.getCertainMatchThreshold(),
                                                        settings.getRejectThreshold());
        int numberOfSuggestions = settings.getNoOfSuggestions();

        if (names.size() > 1){

            ref = "";

            for (String s : names) {
                ref = ref + " " + s;
            }

        } else {
            ref = names.get(0);
        }

        try {

            List<String> searchResults = passwordManager.search(fuzzySearcher, ref, numberOfSuggestions);

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

                    tio.writeln("[q] Quit");

                    tio.writeln("\nEnter choice:");
                    String line = tio.getLine();
                    choice = choices.get(line);

                    if(line.equalsIgnoreCase("q")){
                        throw new CommandException("Quit");
                    }

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

            char[] masterPassword = promptMasterPassword();

            char[] masterKey = passwordManager.getMasterKey(masterPassword);
            byte[] pass = passwordManager.getPassword(ref, masterKey);

            tio.writeln("\nYour password is:");
            tio.writeSecureLn(pass);
            tio.writeln("");

        } catch (RefNotFoundException e) {

            throw new CommandException("Couldn't find password with reference: " + ref);

        } catch (InvalidKeyException e) {

            throw new CommandException("Invalid master password");

        }

    }

}
