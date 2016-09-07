package com.xdrop.passlock.datasource.sqlite;

import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.datasource.Datasource;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.search.FuzzySearcher;

public class SQLiteAESDatasource implements Datasource<AESEncryptionData> {

    public PasswordEntry<AESEncryptionData> getPass(String ref) {
        return null;
    }

    public PasswordEntry<AESEncryptionData> getPass(String fuzzyRef, FuzzySearcher fuzzySearcher) {
        return null;
    }

    public void delPass(String ref) {

    }

    public void updatePass(String ref, PasswordEntry newPasswordEntry) {

    }

    public void addPass(String ref, PasswordEntry<AESEncryptionData> passwordEntry) {

    }

    public void addPass(String ref, String encryptedPass, String desc) {

    }

    @Override
    public void initialize() {

    }
}
