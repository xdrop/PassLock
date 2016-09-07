package com.xdrop.passlock.model;

public abstract class EncryptionData {

    private byte[] encryptedPayload;

    private byte[] salt;

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
