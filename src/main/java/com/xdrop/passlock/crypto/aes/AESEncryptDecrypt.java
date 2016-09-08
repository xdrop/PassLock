package com.xdrop.passlock.crypto.aes;

import com.xdrop.passlock.utils.ByteUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

public class AESEncryptDecrypt {

    private static final Logger LOG = LoggerFactory.getLogger(AESEncryptDecrypt.class);

    private static final String cipherType = "AES/CBC/PKCS5Padding";


    /**
     * Encrypts a given payload using the secret key provided.
     *
     * @param payload The byte array of data to be encrypted
     * @param key The key with which the data should be encrypted
     * @return The encrypted payload and the IV vector
     */
    public AESEncryptionData encrypt(byte[] payload, SecretKey key) {

        try {

            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

            byte[] output = cipher.doFinal(payload);

            AESEncryptionData aesEncryptionData = new AESEncryptionData();
            aesEncryptionData.setInitilizationVector(iv);
            aesEncryptionData.setEncryptedPayload(output);

            return aesEncryptionData;

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

    /**
     * Decrypts an encrypted payload into the original byte array.
     *
     * @param aesOutput Contains the Initialization Vector and the encrypted output
     * @param secretKey The secret key which was used to encrypt
     * @return The raw decrypted data
     * @throws InvalidKeyException Thrown in case the key is invalid
     */
    public byte[] decrypt(AESEncryptionData aesOutput, SecretKey secretKey) throws InvalidKeyException {

        try {

            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(aesOutput.getInitilizationVector()));

            return cipher.doFinal(aesOutput.getEncryptedPayload());

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException e) {

            LOG.info("Failure to decrypt", e);
            return null;

        } catch (BadPaddingException e) {

            throw new InvalidKeyException();

        }

    }


}
