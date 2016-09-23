package me.xdrop.passlock.datasource;

import me.xdrop.passlock.crypto.aes.AESEncryptionData;
import me.xdrop.passlock.exceptions.AlreadyExistsException;
import me.xdrop.passlock.exceptions.NotInitalizedException;
import me.xdrop.passlock.exceptions.RefNotFoundException;
import me.xdrop.passlock.model.BufferedProcessor;
import me.xdrop.passlock.model.EncryptionData;
import me.xdrop.passlock.model.PasswordEntry;

import java.util.Iterator;
import java.util.List;

public interface Datasource <T extends EncryptionData> {

    PasswordEntry<T> getPass(String ref) throws RefNotFoundException;

    List<String> getPassList();

    int getSize();

    int bufferedUpdate(BufferedProcessor<PasswordEntry<T>> bufferedProcessor) throws Exception;

    void delPass(String ref) throws RefNotFoundException;

    void updatePass(String ref, PasswordEntry<AESEncryptionData> newPasswordEntry) throws RefNotFoundException;

    void addPass(String ref, PasswordEntry<T> passwordEntry) throws AlreadyExistsException;

    boolean isCreated();

    void initialize();

    void reset();

}
