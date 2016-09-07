package com.xdrop.passlock.crypto;

import com.xdrop.passlock.model.EncryptionData;

public interface EncryptionModel <T extends EncryptionData>{

    char[] decrypt(T encryptionData);

    T encrypt(char[] data, char[] password);

}
