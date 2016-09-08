package com.xdrop.passlock.crypto.aes;

import com.xdrop.passlock.model.EncryptionData;

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
}
