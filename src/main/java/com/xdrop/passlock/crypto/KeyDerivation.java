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

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";

    /* The key length */
    private static final int KEY_BYTES = 128;

    /* The number of iterations PBKDF2 should perfom */
    private static final int ITERATIONS = 3000;

    /* The default salt size */
    private static final int SALT_BYTES = 8;


    /**
     * Generates a random salt of given length
     *
     * @return
     */
    public static byte[] generateSalt(int length){

        SecureRandom secureRandom = new SecureRandom();

        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);

        return salt;

    }

    /**
     * Generates a random salt of default length
     *
     * @return
     */
    public static byte[] generateSalt(){
        return generateSalt(SALT_BYTES);
    }

    /**
     * Derives a PBEKeySpec from the given password and salt
     *
     * @param password
     * @param salt
     * @return
     */
    private static PBEKeySpec deriveKeySpec(char[] password, byte[] salt){
        return new PBEKeySpec(password, salt, ITERATIONS, KEY_BYTES);
    }

    /**
     * Generates an AES SecretKey through via a password and salt. The secret is
     * consistent as long as the password and salt passed are consistent.
     *
     * @param password
     * @param salt
     * @return
     */
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
