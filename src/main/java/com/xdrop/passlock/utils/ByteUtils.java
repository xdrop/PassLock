package com.xdrop.passlock.utils;

import java.nio.charset.StandardCharsets;

public class ByteUtils {

    public static byte[] getBytes(char[] newPassword) {
        return new String(newPassword).getBytes(StandardCharsets.UTF_8);
    }

}
