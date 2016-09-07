package com.xdrop.passlock.datasource;

import com.xdrop.passlock.model.EncryptionData;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.search.FuzzySearcher;

public interface Datasource {

    PasswordEntry<? extends EncryptionData> getPass(String ref);

    PasswordEntry<? extends EncryptionData> getPass(String fuzzyRef, FuzzySearcher fuzzySearcher);

    void delPass(String ref);

    void updatePass(String ref, PasswordEntry<? extends EncryptionData> newPasswordEntry);

    void addPass(String ref, String encryptedPass);

    void addPass(String ref, String encryptedPass, String desc);

    void initialize();

}
