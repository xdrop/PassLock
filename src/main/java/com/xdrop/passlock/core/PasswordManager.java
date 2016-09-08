package com.xdrop.passlock.core;

import com.xdrop.passlock.crypto.EncryptionModel;
import com.xdrop.passlock.model.EncryptionData;

public interface PasswordManager<T extends EncryptionModel<E>, E extends EncryptionData> {
    void addPassword(String description, char[] newPassword, char[] masterPass, String reference);

    void initializeDatasource(char[] master);
}
