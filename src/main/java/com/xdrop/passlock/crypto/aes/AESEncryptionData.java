package com.xdrop.passlock.crypto.aes;

import com.xdrop.passlock.model.EncryptionData;

public class AESEncryptionData extends EncryptionData {

    private byte[] initilizationVector;


    public byte[] getInitilizationVector() {
        return initilizationVector;
    }

    public void setInitilizationVector(byte[] initilizationVector) {
        this.initilizationVector = initilizationVector;
    }
}
