package com.xdrop.passlock.crypto.aes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESDecrypt {

    private static final Logger LOG = LoggerFactory.getLogger(AESDecrypt.class);

    private static final String cipherType = "AES/CBC/PKCS5Padding";


    /**
     * Decrypts an encrypted payload into the original byte array.
     *
     * @param aesOutput Contains the Initialization Vector and the encrypted output
     * @param secretKey The secret key which was used to encrypt
     * @return The raw decrypted data
     * @throws InvalidKeyException Thrown in case the key is invalid
     */
    public byte[] decrypt(AESOutput aesOutput, SecretKey secretKey) throws InvalidKeyException {

        try {

            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(aesOutput.getIv()));

            return cipher.doFinal(aesOutput.getOutput());

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException |
                BadPaddingException e) {

            LOG.info("Failure to decrypt", e);
            return null;

        }

    }


}
