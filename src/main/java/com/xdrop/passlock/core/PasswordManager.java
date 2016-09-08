package com.xdrop.passlock.core;
import org.apache.commons.codec.binary.Base64;


import com.xdrop.passlock.crypto.EncryptionModel;
import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.crypto.aes.AESEncryptionModel;
import com.xdrop.passlock.datasource.Datasource;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.model.EncryptionData;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.utils.ByteUtils;

public class PasswordManager {

    public void addPassword(String description, char[] newPassword, char[] masterPass, String reference){

        EncryptionModel<AESEncryptionData> encryptionModel = new AESEncryptionModel();

        AESEncryptionData encryptionData = encryptionModel.encrypt(ByteUtils.getBytes(newPassword), masterPass);

        PasswordEntry<AESEncryptionData> passwordEntry = new PasswordEntry<>();
        passwordEntry.setDescription(description);
        passwordEntry.setRef(reference);
        passwordEntry.setEncryptionData(encryptionData);

        Datasource<AESEncryptionData> datasource = new SQLiteAESDatasource();
        datasource.addPass(reference, passwordEntry);


        byte[] b64 = Base64.encodeBase64(encryptionData.getEncryptedPayload());

        System.out.println(new String(b64));

    }



}
