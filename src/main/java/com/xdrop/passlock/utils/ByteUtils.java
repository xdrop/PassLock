package com.xdrop.passlock.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class ByteUtils {

    public static byte[] getBytes(char[] newPassword) {
        return new String(newPassword).getBytes(StandardCharsets.UTF_8);
    }

    public static String toBase64(byte[] bytes){
        byte[] base64 = Base64.encodeBase64(bytes);
        return new String(base64);
    }

    public static byte[] fromBase64(String base64){
        return Base64.decodeBase64(base64.getBytes());
    }

}
