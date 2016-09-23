package me.xdrop.passlock.crypto.aes;

import me.xdrop.passlock.model.EncryptionData;

public class AESEncryptionData extends EncryptionData {

    private byte[] initilizationVector;

    public AESEncryptionData(byte[] encryptedPayload, byte[] salt, byte[] initializationVector) {
        super(encryptedPayload, salt);
        this.initilizationVector = initializationVector;
    }

    public AESEncryptionData() {
        super();
    }

    public byte[] getInitilizationVector() {
        return initilizationVector;
    }

    public void setInitilizationVector(byte[] initilizationVector) {
        this.initilizationVector = initilizationVector;
    }

    public AESEncryptionData from(EncryptionData encData) {

        AESEncryptionData aesEncryptionData = new AESEncryptionData();

        aesEncryptionData.setSalt(encData.getSalt());
        aesEncryptionData.setEncryptedPayload(encData.getEncryptedPayload());

        return aesEncryptionData;

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        AESEncryptionData clone = from((EncryptionData) super.clone());

        clone.setInitilizationVector(this.getInitilizationVector());

        return clone;

    }
}
