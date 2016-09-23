package me.xdrop.passlock.core

import me.xdrop.passlock.LogGroovyTestCase
import me.xdrop.passlock.datasource.sqlite.SQLiteAESDatasource
import me.xdrop.passlock.exceptions.RefNotFoundException
import me.xdrop.passlock.search.DefaultSearch
import me.xdrop.passlock.utils.ByteUtils

import java.security.InvalidKeyException

class PasswordManagerAESIT extends LogGroovyTestCase {

    def pwman = new PasswordManagerAES(new SQLiteAESDatasource("store.db"));
    def masterPass = "mymaster";
    def masterPassC = "mymaster".toCharArray();
    def encryptionPayload = "encryptme"

    void setUp() {

        super.setUp()
        pwman.initializeDatasource(masterPass.toCharArray())

        pwman.addPassword("Description", encryptionPayload.getBytes("UTF-8"), "nonaesmaster".toCharArray(), "def")
        pwman.addPassword("Description", encryptionPayload.getBytes("UTF-8"), pwman.getMasterKey(masterPassC), "real")

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

        def gotten  = pwman.getPassword("def", "nonaesmaster".toCharArray())
        def gotten2 = pwman.getPassword("real", pwman.getMasterKey(masterPassC))

        assertNotNull gotten
        assertNotNull gotten2
        assert gotten  == encryptionPayload.getBytes("UTF-8")
        assert gotten2 == encryptionPayload.getBytes("UTF-8")

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

        shouldFail(RefNotFoundException) {
            pwman.rename("master","anything")
        }

    }

    void testUpdate() {

        pwman.updatePassword("def", "nonaesmaster".toCharArray(), "newpass".toCharArray())

        assertEquals "newpass".getBytes("UTF-8"), pwman.getPassword("def", "nonaesmaster".toCharArray())

    }

    void testUpdateMaster() {

        def sampleStored = "hi".toCharArray()
        def sampleStoredBytes = "hi".getBytes("UTF-8")

        print sampleStoredBytes
        print ByteUtils.getBytes(sampleStored)

        assert pwman.unlocksMaster(masterPassC)

        pwman.addPassword("", sampleStored, pwman.getMasterKey(masterPassC),"ab")
        pwman.addPassword("", sampleStored, pwman.getMasterKey(masterPassC),"cd")
        pwman.addPassword("", sampleStored, pwman.getMasterKey(masterPassC),"ef")

        print "\n\n\n\n" + pwman.getPassword("ab", pwman.getMasterKey(masterPassC))

        def newC = "newmaster".toCharArray()

        pwman.updateMasterPassword(masterPassC, newC)

        assert pwman.getPassword("ab", pwman.getMasterKey(newC)) == sampleStoredBytes

    }

    void testDelete() {

        pwman.deletePassword("def")

        shouldFail(RefNotFoundException) {

            pwman.getPassword("def")
            pwman.deletePassword("master")

        }

    }

}