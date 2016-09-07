package com.xdrop.passlock.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class KeyDerivation {


    private static final Logger LOG = LoggerFactory.getLogger(KeyDerivation.class);

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";

    /* The size in bytes of the salt and the hash */
    private static final int SALT_BYTES = 8;
    private static final int KEY_BYTES = 128;
    private static final int ITERATIONS = 3000;



    private static byte[] generateSalt(int bytes) {

        // Generate a random salt
        SecureRandom random = new SecureRandom();

        // Serve the bytes in a fresh array
        byte[] salt = new byte[bytes];
        random.nextBytes(salt);

        return salt;
    }


    private static PBEKeySpec deriveKeySpec(char[] password, byte[] salt){
        return new PBEKeySpec(password, salt, ITERATIONS, KEY_BYTES);
    }

    public static SecretKey generateAESSecret(char[] password, byte[] salt){

        try {

            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            PBEKeySpec keySpec = deriveKeySpec(password, salt);
            SecretKey tmp = secretKeyFactory.generateSecret(keySpec);

            return new SecretKeySpec(tmp.getEncoded(),"AES");

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOG.info("Failed to generate AES secret", e);
            return null;
        }
    }


}
