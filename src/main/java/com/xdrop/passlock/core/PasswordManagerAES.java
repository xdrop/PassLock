package com.xdrop.passlock.core;

import com.xdrop.passlock.exceptions.RefNotFoundException;


import com.xdrop.passlock.crypto.EncryptionModel;
import com.xdrop.passlock.crypto.aes.AESEncryptionData;
import com.xdrop.passlock.crypto.aes.AESEncryptionModel;
import com.xdrop.passlock.datasource.Datasource;
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

    public PasswordManagerAES(Datasource<AESEncryptionData> datasource) {

        this.datasource = datasource;
        encryptionModel = new AESEncryptionModel();

    }

    /**
     * Stores and encrypts the given password, using an AES256 key
     * derived from the master password which is stored
     * in the datasource.
     *
     * @param description Description of what this password is for
     * @param newPassword The password to store in a *char* array
     * @param masterPass  The master password
     * @param reference   A unique reference identifier for this entry
     */
    @Override
    public void addPassword(String description, char[] newPassword, char[] masterPass, String reference) {
        addPassword(description, ByteUtils.getBytes(newPassword), masterPass, reference);
    }

    /**
     * Stores and encrypts the given password, using an AES256 key
     * derived from the master password which is stored
     * in the datasource.
     *
     * @param description Description of what this password is for
     * @param newPassword The password to store in *bytes*
     * @param masterPass  The master password
     * @param reference   A unique reference identifier for this entry
     */
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

    /**
     * Retrieves and decrypts the password requested, using an AES256
     * key which is retrieved from the datastore using the
     * master password.
     *
     * @param reference A unique reference identifier for this entry
     * @param password  The password with which this should be decrypted
     * @return The decrypted password in byte[] UTF-8 format
     * @throws RefNotFoundException Thrown if the reference used
     *                              could not be found in the
     *                              database.
     * @throws InvalidKeyException  Thrown if the password supplied
     *                              is incorrect and cannot
     *                              unlock the password.
     */
    @Override
    public byte[] getPassword(String reference, char[] password)
            throws RefNotFoundException, InvalidKeyException {

        LOG.debug("Looking for " + reference + "...");
        PasswordEntry<AESEncryptionData> pass = datasource.getPass(reference);

        LOG.debug("Decrypting with key...");
        return encryptionModel.decrypt(pass.getEncryptionData(), password);

    }

    /**
     * Initializes and *resets* the database. All data in it will be lost!
     *
     * @param masterPass The master password to initialize the database
     *                   with.
     */
    @Override
    public void initializeDatasource(char[] masterPass) {

        LOG.info("Initializing datasource...");

        /* Initialize the datasource */
        datasource.reset();
        datasource.initialize();

        LOG.info("Generating AES secret...");

        /* Store the master key */
        SecretKey secretKey = encryptionModel.generateSecret(masterPass);

        addPassword("The master key", secretKey.getEncoded(), masterPass, "master");

        LOG.info("AES secret succesfully stored!");

    }

    /**
     * Determines whether the password supplied can unlock the master key
     * (AES256)
     *
     * @param password The master password used to encrypt the master
     *                 key.
     * @return True  - if password is correct
     * False - otherwise
     */
    @Override
    public boolean unlocksMaster(char[] password) {

        try {

            getPassword("master", password);
            return true;

        } catch (RefNotFoundException e) {

            LOG.error("Master key not found", e);
            return false;

        } catch (InvalidKeyException e) {

            return false;

        }

    }

    /**
     * Returns the stored (and encrypted) master key (AES256)
     *
     * @param password The master password used to encrypt the master
     *                 key.
     * @return The master key in a char[] array
     * @throws InvalidKeyException If the password used is not correct
     */
    @Override
    public char[] getMasterKey(char[] password) throws InvalidKeyException {

        try {

            LOG.debug("Retrieving master key...");

            return ByteUtils.getChars(getPassword("master", password));

        } catch (RefNotFoundException e) {

            LOG.error("Master key not found", e);
            return null;

        }

    }

    public void setDatasource(Datasource<AESEncryptionData> datasource) {
        this.datasource = datasource;
    }


    public void setEncryptionModel(EncryptionModel<AESEncryptionData> encryptionModel) {
        this.encryptionModel = encryptionModel;
    }
}
