package com.xdrop.passlock.core

import com.xdrop.passlock.LogGroovyTestCase
import com.xdrop.passlock.crypto.aes.AESEncryptionData
import com.xdrop.passlock.crypto.aes.AESEncryptionModel
import com.xdrop.passlock.datasource.Datasource
import com.xdrop.passlock.utils.ByteUtils


import static org.easymock.EasyMock.*;
import com.xdrop.passlock.model.PasswordEntry

import java.security.InvalidKeyException

class PasswordManagerAESTest extends LogGroovyTestCase {

    def encryptionPayload = "encryptme"
    def nonMasterPass = "nonaesmaster".toCharArray()

    void setUp() {

        super.setUp()

    }

    void testAddPassword() {

        def ds = mock(Datasource.class)
        def pwman = new PasswordManagerAES(ds)

        expect(ds.addPass(eq("test"), anyObject(PasswordEntry)))
        replay(ds)

        pwman.addPassword("Description", "testpayload".toCharArray(), nonMasterPass, "test")

    }

    void testGetPassword() {

        def encModel = mock(AESEncryptionModel.class)
        def ds = mock(Datasource.class)
        def wrongPass = "wrongpass".getChars()
        def pwman = new PasswordManagerAES(ds)

        expect(ds.getPass("test"))
                .andReturn(new PasswordEntry())
                .anyTimes()

        expect(encModel.decrypt(isNull(AESEncryptionData), eq(nonMasterPass)))
                .andReturn(encryptionPayload.getBytes("UTF-8"))
                .anyTimes()

        expect(encModel.decrypt(isNull(AESEncryptionData), eq(wrongPass)))
                .andThrow(new InvalidKeyException())
                .anyTimes()

        replay(encModel, ds)

        // inject mocks
        pwman.setEncryptionModel(encModel)


        def gotten = pwman.getPassword("test", false, nonMasterPass)

        assertNotNull gotten

        assert gotten == encryptionPayload.getBytes("UTF-8")

        shouldFail(InvalidKeyException) {
            pwman.getPassword("test", false, wrongPass)
        }

    }

    void testInitializeDatasource() {

        def ds = mock(Datasource)
        def pwman = new PasswordManagerAES(ds)

        expect(ds.reset())
        expect(ds.initialize())
        expect(ds.addPass(eq("master"), anyObject(PasswordEntry.class)))

        replay(ds)

        pwman.initializeDatasource(nonMasterPass)

    }

    void testUnlocksMaster() {

        def mypass = "mypass".toCharArray()
        def wrong = "wrongpass".toCharArray()


        def passman = partialMockBuilder(PasswordManagerAES.class)
                .addMockedMethod("getPassword")
                .createMock();


        expect(passman.getPassword("master", false, mypass))
            .andReturn(new byte[12])
            .anyTimes()

        expect(passman.getPassword("master", false, wrong))
            .andThrow(new InvalidKeyException())
            .anyTimes()


        replay(passman)

        assertTrue passman.unlocksMaster(mypass)
        assertFalse passman.unlocksMaster(wrong)

    }

    void testGetMasterKey() {

        def passman = partialMockBuilder(PasswordManagerAES.class)
                .addMockedMethod("getPassword")
                .createMock();

        def mypass = "mypassword".toCharArray()
        def storedKey = "masterpass".getBytes("UTF-8")

        expect(passman.getPassword("master", false, mypass))
                .andReturn(storedKey)

        replay(passman)

        def masterKey = passman.getMasterKey(mypass)


        assertNotNull masterKey
        assert masterKey == ByteUtils.getChars(storedKey)

    }
}
