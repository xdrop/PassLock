package com.xdrop.passlock.crypto.aes;

import com.xdrop.passlock.crypto.EncryptionModel;
import com.xdrop.passlock.crypto.KeyDerivation;
import com.xdrop.passlock.utils.ByteUtils;
import com.xdrop.passlock.utils.KeyUtils;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;

public class AESEncryptionModel implements EncryptionModel<AESEncryptionData> {

    @Override
    public byte[] decrypt(AESEncryptionData encryptionData, char[] password) throws InvalidKeyException {

        SecretKey secretKey = KeyDerivation.generateAESSecret(password, encryptionData.getSalt());

        return new AESEncryptDecrypt().decrypt(encryptionData,secretKey);
    }

    @Override
    public AESEncryptionData encrypt(byte[] data, char[] password) {

        byte[] salt = KeyDerivation.generateSalt();
        SecretKey secretKey = KeyDerivation.generateAESSecret(password, salt);

        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
        AESEncryptionData encryptionData = aesEncryptDecrypt.encrypt(data, secretKey);
        encryptionData.setSalt(salt);

        return encryptionData;
    }
}
