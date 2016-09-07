package com.xdrop.passlock.crypto.aes;

public class AESOutput {

    private byte[] output;

    private byte[] iv;

    public AESOutput(byte[] output, byte[] iv) {
        this.output = output;
        this.iv = iv;
    }

    public byte[] getOutput() {
        return output;
    }

    public byte[] getIv() {
        return iv;
    }
}
