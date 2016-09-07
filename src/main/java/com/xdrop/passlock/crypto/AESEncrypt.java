package com.xdrop.passlock.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

public class AESEncrypt {

    private static final Logger LOG = LoggerFactory.getLogger(AESEncrypt.class);

    private static final String cipherType = "AES/CBC/PKCS5Padding";


    /**
     * Encrypts a given payload using the secret key provided.
     *
     * @param payload The byte array of data to be encrypted
     * @param key The key with which the data should be encrypted
     * @return The encrypted payload and the IV vector
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public AESOutput encrypt(byte[] payload, SecretKey key) {

        try {

            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

            byte[] output = cipher.doFinal(payload);

            return new AESOutput(output, iv);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                BadPaddingException |
                IllegalBlockSizeException |
                InvalidParameterSpecException e) {

            LOG.info("Failed to encrypt", e);
            return null;

        }

    }


}
