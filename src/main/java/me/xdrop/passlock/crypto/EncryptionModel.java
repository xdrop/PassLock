package me.xdrop.passlock.crypto;

import me.xdrop.passlock.model.EncryptionData;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;

public interface EncryptionModel <T extends EncryptionData>{

    byte[] decrypt(T encryptionData, char[] password) throws InvalidKeyException;

    T encrypt(byte[] data, char[] password);

    SecretKey generateSecret(char[] password);

}
