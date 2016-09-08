package com.xdrop.passlock.core;

import com.xdrop.passlock.crypto.EncryptionModel;
import com.xdrop.passlock.exceptions.RefNotFoundException;
import com.xdrop.passlock.model.EncryptionData;

import java.security.InvalidKeyException;

public interface PasswordManager<T extends EncryptionModel<E>, E extends EncryptionData> {

    void addPassword(String description, char[] newPassword, char[] masterPass, String reference);

    void addPassword(String description, byte[] newPassword, char[] masterPass, String reference);

    byte[] getPassword(String ref, boolean searchFuzzy, char[] password) throws RefNotFoundException, InvalidKeyException;

    void initializeDatasource(char[] masterPass);

    boolean unlocksMaster(char[] password);

    char[] getMasterKey(char[] password) throws InvalidKeyException;

}
