package me.xdrop.passlock.core;

import me.xdrop.passlock.crypto.EncryptionModel;
import me.xdrop.passlock.crypto.aes.AESEncryptionData;
import me.xdrop.passlock.datasource.Datasource;
import me.xdrop.passlock.exceptions.AlreadyExistsException;
import me.xdrop.passlock.exceptions.RefNotFoundException;
import me.xdrop.passlock.model.EncryptionData;
import me.xdrop.passlock.search.FuzzySearcher;

import java.security.InvalidKeyException;
import java.util.List;

public interface PasswordManager<T extends EncryptionModel<E>, E extends EncryptionData> {

    /**
     * Stores and encrypts the given password, using an encryption algorithm
     *
     * @param description Description of what this password is for
     * @param newPassword The password to store in a {@code char} array
     * @param masterPass  The master password
     * @param reference   A unique reference identifier for this entry
     * @throws AlreadyExistsException Thrown if the target reference already
     *                                exists
     */
    void addPassword(String description, char[] newPassword, char[] masterPass, String reference)
            throws AlreadyExistsException;


    /**
     * Stores and encrypts the given password, using an encryption algorithm
     *
     * @param description Description of what this password is for
     * @param newPassword The password to store in a {@code byte} array
     * @param masterPass  The master password
     * @param reference   A unique reference identifier for this entry
     * @throws AlreadyExistsException Thrown if the target reference already
     *                                exists
     */
    void addPassword(String description, byte[] newPassword, char[] masterPass, String reference)
            throws AlreadyExistsException;

    /**
     * Retrieves and decrypts the password requested, using a decryption algorithm
     *
     * @param reference A unique reference identifier for this entry
     * @param masterKey The master key with which this should be decrypted
     * @return The decrypted password in byte[] UTF-8 format
     * @throws RefNotFoundException Thrown if the reference used
     *                              could not be found in the
     *                              database.
     * @throws InvalidKeyException  Thrown if the password supplied
     *                              is incorrect and cannot
     *                              unlock the password.
     */
    byte[] getPassword(String reference, char[] masterKey)
            throws RefNotFoundException, InvalidKeyException;

    /**
     * Renames a password reference to another
     *
     * @param reference    Old reference
     * @param newReference New reference
     * @throws RefNotFoundException Thrown if the old reference
     *                              doesn't exist
     */
    void rename(String reference, String newReference) throws RefNotFoundException;

    /**
     * Updates a password
     *
     * @param reference   Reference to the password
     * @param masterKey   The master key used to encrypt the new enty
     * @param newPassword The new password to store
     * @throws RefNotFoundException Thrown if the reference doesn't exist
     */
    void updatePassword(String reference, char[] masterKey, char[] newPassword) throws RefNotFoundException;

    /**
     * Deletes a password
     *
     * @param reference Reference to the password
     * @throws RefNotFoundException Thrown if the reference doesn't exist
     */
    void deletePassword(String reference) throws RefNotFoundException;

    /**
     * Initializes and *resets* the database. All data in it will be lost!
     *
     * @param masterPass The master password to initialize the database
     *                   with.
     */
    void initializeDatasource(char[] masterPass);

    /**
     * Checks whether the datasource is already initialized
     *
     * @return True if datasource is initialized
     */
    boolean isInitialized();

    /**
     * Determines whether the password supplied can unlock the master key
     *
     * @param password The master password used to encrypt the master
     *                 key.
     * @return True  - if password is correct
     * False - otherwise
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

    /**
     * Checks if this reference exists
     *
     * @param ref Input reference
     * @return True if exists
     */
    boolean exists(String ref);

    /**
     * Lists all the passwords
     * @return List of passwords
     */
    List<String> list();

    /**
     * Searches the datasource to find a password entry matching
     * {@code ref}, and returns the list of the closest
     * matches.
     *
     * @param searcher The searching class to be used
     * @param query    The query string to search for
     * @param limit    The number of entries to return
     * @return The matched password references
     * @throws RefNotFoundException Thrown if the query failed to match any
     *                              entries with the specified cutoff level
     */
    List<String> search(FuzzySearcher searcher, String query, int limit) throws RefNotFoundException;

    /**
     * Copies a password
     *
     * @param from Source password
     * @param to   Target password
     * @throws RefNotFoundException   Thrown if the old reference
     *                                doesn't exist
     * @throws AlreadyExistsException Thrown if the new reference already
     *                                exists
     */
    void copy(String from, String to) throws RefNotFoundException, AlreadyExistsException;

    /**
     * Sets the datasource
     * @param datasource Datasource
     */
    void setDatasource(Datasource<AESEncryptionData> datasource);
}
