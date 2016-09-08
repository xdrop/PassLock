package com.xdrop.passlock.crypto.aes;

import com.xdrop.passlock.model.EncryptionData;
import com.xdrop.passlock.utils.ByteUtils;

public class AESEncryptionData extends EncryptionData {

    private byte[] initilizationVector;


    public AESEncryptionData(byte[] encryptedPayload, byte[] salt, byte[] initializationVector) {
        super(encryptedPayload, salt);
        this.initilizationVector = initializationVector;
    }

    public AESEncryptionData() {
        super();
        this.initilizationVector = ByteUtils.dummyByte();
    }

    public byte[] getInitilizationVector() {
        return initilizationVector;
    }

    public void setInitilizationVector(byte[] initilizationVector) {
        this.initilizationVector = initilizationVector;
    }
}
