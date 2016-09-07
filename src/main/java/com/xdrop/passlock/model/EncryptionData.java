package com.xdrop.passlock.model;

public abstract class EncryptionData {

    private char[] encryptedPayload;

    private char[] salt;

    public char[] getEncryptedPayload() {
        return encryptedPayload;
    }

    public char[] getSalt() {
        return salt;
    }

    public void setSalt(char[] salt) {
        this.salt = salt;
    }

    public void setEncryptedPayload(char[] encryptedPayload) {
        this.encryptedPayload = encryptedPayload;
    }
}
