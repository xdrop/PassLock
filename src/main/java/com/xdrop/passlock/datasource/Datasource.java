package com.xdrop.passlock.datasource;

import com.xdrop.passlock.model.EncryptionData;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.search.FuzzySearcher;

public interface Datasource <T extends EncryptionData> {

    PasswordEntry<T> getPass(String ref);

    PasswordEntry<T> getPass(String fuzzyRef, FuzzySearcher fuzzySearcher);

    void delPass(String ref);

    void updatePass(String ref, PasswordEntry<T> newPasswordEntry);

    void addPass(String ref, PasswordEntry<T> passwordEntry);

    void initialize();

}
