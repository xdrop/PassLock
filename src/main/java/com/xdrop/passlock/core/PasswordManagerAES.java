package com.xdrop.passlock.core;

import com.xdrop.passlock.exceptions.RefNotFoundException;


import com.xdrop.passlock.crypto.EncryptionModel;
import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.crypto.aes.AESEncryptionModel;
import com.xdrop.passlock.datasource.Datasource;
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource;
import com.xdrop.passlock.model.PasswordEntry;
import com.xdrop.passlock.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;

public class PasswordManagerAES implements PasswordManager<AESEncryptionModel, AESEncryptionData> {

    private final static Logger LOG = LoggerFactory.getLogger(PasswordManagerAES.class);


    private EncryptionModel<AESEncryptionData> encryptionModel;

    private Datasource<AESEncryptionData> datasource;

    public PasswordManagerAES() {

        datasource = new SQLiteAESDatasource();
        encryptionModel = new AESEncryptionModel();

    }

    @Override
    public void addPassword(String description, char[] newPassword, char[] masterPass, String reference) {

        addPassword(description, ByteUtils.getBytes(newPassword), masterPass, reference);

    }

    @Override
    public void addPassword(String description, byte[] newPassword, char[] masterPass, String reference) {

        LOG.debug("Encrypting entry [" + reference + "]...");

        AESEncryptionData encryptionData = encryptionModel.encrypt(newPassword, masterPass);

        PasswordEntry<AESEncryptionData> passwordEntry = new PasswordEntry<>();
        passwordEntry.setDescription(description);
        passwordEntry.setRef(reference);
        passwordEntry.setEncryptionData(encryptionData);

        LOG.debug("Storing in datasource...");

        datasource.addPass(reference, passwordEntry);

    }

    @Override
    public byte[] getPassword(String ref, boolean searchFuzzy, char[] password)
            throws RefNotFoundException, InvalidKeyException {

        LOG.debug("Looking for " + ref +"...");

        if (!searchFuzzy) {

            PasswordEntry<AESEncryptionData> pass = datasource.getPass(ref);

            LOG.debug("Decrypting with key...");

            return encryptionModel.decrypt(pass.getEncryptionData(), password);

        }

        return new byte[0];
    }

    @Override
    public void initializeDatasource(char[] master) {

        LOG.info("Initializing datasource...");

        /* Initialize the datasource */
        datasource.reset();
        datasource.initialize();

        LOG.info("Generating AES secret...");

        /* Store the master key */
        SecretKey secretKey = encryptionModel.generateSecret(master);

        addPassword("The master key", secretKey.getEncoded(), master, "master");

        LOG.info("AES secret succesfully stored!");

    }

    @Override
    public boolean unlocksMaster(char[] master) {

        try {

            getPassword("master", false, master);
            return true;

        } catch (RefNotFoundException e) {

            LOG.error("Master key not found", e);
            return false;

        } catch (InvalidKeyException e) {

            return false;

        }

    }

    @Override
    public char[] getMasterKey(char[] password) throws InvalidKeyException {

        try {

            LOG.debug("Retrieving master key...");

            return ByteUtils.getChars(getPassword("master", false, password));

        } catch (RefNotFoundException e) {

            LOG.error("Master key not found", e);
            return null;

        }

    }


}
