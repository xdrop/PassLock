package com.xdrop.passlock.core

import com.xdrop.passlock.LogGroovyTestCase
import com.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource
import com.xdrop.passlock.exceptions.RefNotFoundException
import com.xdrop.passlock.search.DefaultSearch

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

    void testSearch() {

        pwman.addPassword("Description", encryptionPayload.getBytes("UTF-8"), "nonaesmaster".toCharArray(),
                "www.google.com")

        assertEquals "www.google.com", pwman.search(new DefaultSearch(), "google", 5).get(0)

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
            pwman.getPassword("def", "asda".toCharArray())
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

    void testRename() {

        pwman.rename("def", "new")

        assert "encryptme".getBytes("UTF-8") == pwman.getPassword("new", "nonaesmaster".toCharArray())

    }

    void testUpdate() {

        pwman.updatePassword("def", "nonaesmaster".toCharArray(), "newpass".toCharArray())

        assertEquals "newpass".getBytes("UTF-8"), pwman.getPassword("def", "nonaesmaster".toCharArray())

    }

    void testDelete() {

        pwman.deletePassword("def")

        shouldFail(RefNotFoundException) {

            pwman.getPassword("def")

        }

    }

}