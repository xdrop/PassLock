package com.xdrop.passlock.model;

import com.xdrop.passlock.utils.ByteUtils;

public class EncryptionData {

    private byte[] encryptedPayload = new byte[1];

    private byte[] salt = new byte[1];


    public EncryptionData(byte[] encryptedPayload, byte[] salt) {
        this.encryptedPayload = encryptedPayload;
        this.salt = salt;
    }

    public EncryptionData() {
        this.encryptedPayload = ByteUtils.dummyByte();
        this.encryptedPayload = ByteUtils.dummyByte();
    }

    public byte[] getEncryptedPayload() {
        return encryptedPayload;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void setEncryptedPayload(byte[] encryptedPayload) {
        this.encryptedPayload = encryptedPayload;
    }
}
