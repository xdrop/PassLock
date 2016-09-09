package com.xdrop.passlock.core

import com.xdrop.passlock.LogGroovyTestCase
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource

import java.security.InvalidKeyException

class PasswordManagerAESIT extends LogGroovyTestCase {

    def pwman = new PasswordManagerAES(new SQLiteAESDatasource());
    def masterPass = "mymaster";
    def encryptionPayload = "encryptme"

    void setUp() {

        super.setUp()
        pwman.initializeDatasource(masterPass.toCharArray())

        pwman.addPassword("Description", encryptionPayload.getBytes("UTF-8"), "nonaesmaster".toCharArray(), "def")

    }

    void testAddPassword() {

        pwman.addPassword("Description", "testpayload".toCharArray(), "nonaesmaster".toCharArray(), "test")

        assertNotNull pwman.getPassword("test", "nonaesmaster".toCharArray())

    }

    void testGetPassword() {

        def gotten = pwman.getPassword("def", "nonaesmaster".toCharArray())

        assertNotNull gotten
        assert gotten == encryptionPayload.getBytes("UTF-8")

        shouldFail(InvalidKeyException){
            pwman.getPassword("def", "wrongpassword23132@@\$£:\$%@\$^%:^@\$%&\$&:\$%^\$%\\^".toCharArray())
        }

    }

    void testInitializeDatasource() {

        def master = pwman.getPassword("master", "mymaster".getChars())

        assertNotNull master

    }

    void testUnlocksMaster() {

        assertTrue pwman.unlocksMaster("mymaster".getChars())
        assertFalse pwman.unlocksMaster("mymastern".getChars())

    }

    void testGetMasterKey() {

        def masterKey = pwman.getMasterKey(masterPass.toCharArray())

        pwman.addPassword("New pass", "hideme".getChars(), masterKey, "newpass")

        assertNotNull masterKey
        assert pwman.getPassword("newpass", masterKey) == "hideme".getBytes("UTF-8")

    }
}