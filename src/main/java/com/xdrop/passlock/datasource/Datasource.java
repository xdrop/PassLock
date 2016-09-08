package com.xdrop.passlock.datasource;

import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.exceptions.RefNotFoundException;
import com.xdrop.passlock.model.EncryptionData;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.search.FuzzySearcher;

public interface Datasource <T extends EncryptionData> {

    PasswordEntry<T> getPass(String ref) throws RefNotFoundException;

    PasswordEntry<T> getPass(String fuzzyRef, FuzzySearcher fuzzySearcher) throws RefNotFoundException;

    void delPass(String ref) throws RefNotFoundException;

    void updatePass(String ref, PasswordEntry<AESEncryptionData> newPasswordEntry) throws RefNotFoundException;

    void addPass(String ref, PasswordEntry<T> passwordEntry);

    void initialize();

    void reset();

}
