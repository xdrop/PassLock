package me.xdrop.passlock.model;

public class EncryptionData implements Cloneable {

    private byte[] encryptedPayload;

    private byte[] salt;

    public EncryptionData(byte[] encryptedPayload, byte[] salt) {
        this.encryptedPayload = encryptedPayload;
        this.salt = salt;
    }

    public EncryptionData() {
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

    @Override
    protected Object clone() throws CloneNotSupportedException {

        super.clone();

        EncryptionData clone = new EncryptionData();
        clone.setEncryptedPayload(this.getEncryptedPayload());
        clone.setSalt(this.getSalt());
        return clone;

    }
}
