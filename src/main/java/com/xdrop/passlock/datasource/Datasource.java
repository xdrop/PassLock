package com.xdrop.passlock.datasource;

import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.exceptions.AlreadyExistsException;
import com.xdrop.passlock.exceptions.RefNotFoundException;
import com.xdrop.passlock.model.EncryptionData;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.search.FuzzySearcher;

import java.util.List;

public interface Datasource <T extends EncryptionData> {

    PasswordEntry<T> getPass(String ref) throws RefNotFoundException;

    List<String> getPassList();

    void delPass(String ref) throws RefNotFoundException;

    void updatePass(String ref, PasswordEntry<AESEncryptionData> newPasswordEntry) throws RefNotFoundException;

    void addPass(String ref, PasswordEntry<T> passwordEntry) throws AlreadyExistsException;

    void initialize();

    void reset();

}
