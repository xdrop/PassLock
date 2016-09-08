package com.xdrop.passlock.core;

import com.xdrop.passlock.crypto.EncryptionModel;
import com.xdrop.passlock.exceptions.RefNotFoundException;
import com.xdrop.passlock.model.EncryptionData;

import java.security.InvalidKeyException;

public interface PasswordManager<T extends EncryptionModel<E>, E extends EncryptionData> {

    /**
     * Stores and encrypts the given password, using an encryption algorithm
     *
     * @param description Description of what this password is for
     * @param newPassword The password to store in a *char* array
     * @param masterPass The master password
     * @param reference A unique reference identifier for this entry
     */
    void addPassword(String description, char[] newPassword, char[] masterPass, String reference);

    /**
     * Stores and encrypts the given password, using an encryption algorithm
     *
     * @param description Description of what this password is for
     * @param newPassword The password to store in *bytes*
     * @param masterPass The master password
     * @param reference A unique reference identifier for this entry
     */
    void addPassword(String description, byte[] newPassword, char[] masterPass, String reference);

    /**
     * Retrieves and decrypts the password requested, using a decryption algorithm
     *
     * @param reference A unique reference identifier for this entry
     * @param searchFuzzy Determines whether the datasource should be searched
     *                    fuzzily, ie. will try and determine the closest
     *                    non exact match
     *                    (eg. "goolge" might resolve to www.google.com)
     * @param password The password with which this should be decrypted
     * @return The decrypted password in byte[] UTF-8 format
     * @throws RefNotFoundException Thrown if the reference used
     *                              could not be found in the
     *                              database.
     * @throws InvalidKeyException Thrown if the password supplied
     *                             is incorrect and cannot
     *                             unlock the password.
     */
    byte[] getPassword(String reference, boolean searchFuzzy, char[] password)
            throws RefNotFoundException, InvalidKeyException;

    /**
     * Initializes and *resets* the database. All data in it will be lost!
     *
     * @param masterPass The master password to initialize the database
     *                   with.
     */
    void initializeDatasource(char[] masterPass);

    /**
     * Determines whether the password supplied can unlock the master key
     *
     * @param password The master password used to encrypt the master
     *                 key.
     * @return True  - if password is correct
     *         False - otherwise
     */
    boolean unlocksMaster(char[] password);

    /**
     * Returns the stored (and encrypted) master key
     *
     * @param password The master password used to encrypt the master
     *                 key.
     * @return The master key in a char[] array
     * @throws InvalidKeyException If the password used is not correct
     */
    char[] getMasterKey(char[] password) throws InvalidKeyException;

}
