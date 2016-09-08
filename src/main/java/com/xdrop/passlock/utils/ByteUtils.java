package com.xdrop.passlock.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteUtils {

    public static byte[] getBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

    public static char[] getChars(byte[] bytes){
        char[] chars = new char[bytes.length/2];
        for(int i=0;i<chars.length;i++)
            chars[i] = (char) ((bytes[i*2] << 8) + (bytes[i*2+1] & 0xFF));
        return chars;
    }

    public static String toBase64(byte[] bytes){
        byte[] base64 = Base64.encodeBase64(bytes);
        return new String(base64);
    }

    public static byte[] fromBase64(String base64){
        return Base64.decodeBase64(base64.getBytes());
    }

    public static byte[] dummyByte() {
        return new byte[] { 0x1, 0x1, 0x1, 0x1 };
    }


}
