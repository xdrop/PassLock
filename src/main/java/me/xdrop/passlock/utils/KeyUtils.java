package me.xdrop.passlock.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyUtils {

    public static String secretKeyToString(SecretKey secretKey){
        return Base64.encodeBase64String(secretKey.getEncoded());
    }

    public static SecretKey stringToAESSecretKey(String key) {
        byte[] encodedKey = new Base64().decode(key);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

}
