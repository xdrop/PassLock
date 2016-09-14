package com.xdrop.passlock.core

import com.xdrop.passlock.LogGroovyTestCase
import com.xdrop.passlock.crypto.aes.AESEncryptionData
import com.xdrop.passlock.crypto.aes.AESEncryptionModel
import com.xdrop.passlock.datasource.Datasource
import com.xdrop.passlock.exceptions.RefNotFoundException
import com.xdrop.passlock.utils.ByteUtils
import org.easymock.Capture

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


        def gotten = pwman.getPassword("test", nonMasterPass)

        assertNotNull gotten

        assert gotten == encryptionPayload.getBytes("UTF-8")

        shouldFail(InvalidKeyException) {
            pwman.getPassword("test", wrongPass)
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


        def passman = partialMockBuilder(PasswordManagerAES)
                .addMockedMethod("getPassword")
                .createMock();


        expect(passman.getPassword("master", mypass))
            .andReturn(new byte[12])
            .anyTimes()

        expect(passman.getPassword("master", wrong))
            .andThrow(new InvalidKeyException())
            .anyTimes()


        replay(passman)

        assertTrue passman.unlocksMaster(mypass)
        assertFalse passman.unlocksMaster(wrong)

    }

    void testGetMasterKey() {

        def passman = partialMockBuilder(PasswordManagerAES)
                .addMockedMethod("getPassword")
                .createMock();

        def mypass = "mypassword".toCharArray()
        def storedKey = "masterpass".getBytes("UTF-8")

        expect(passman.getPassword("master", mypass))
                .andReturn(storedKey)

        replay(passman)

        def masterKey = passman.getMasterKey(mypass)


        assertNotNull masterKey
        assert masterKey == ByteUtils.getChars(storedKey)

    }

    void testRename() {

        def ds = mock(Datasource)

        def captured = new Capture<PasswordEntry>()

        expect(ds.getPass(eq("def")))
            .andReturn(new PasswordEntry())

        expect(ds.updatePass(eq("def"), capture(captured)))
            .andVoid()

        replay(ds)


        def pwman = new PasswordManagerAES(ds)

        pwman.rename("def", "new")

        assert captured.value.ref == "new"

    }

    void testUpdate() {

        def ds = mock(Datasource)
        def em = mock(AESEncryptionModel)

        def captured = new Capture<PasswordEntry>()

        expect(ds.getPass(eq("def")))
                .andReturn(new PasswordEntry())

        expect(ds.updatePass(eq("def"), capture(captured)))
                .andVoid()

        byte[] input1 = ByteUtils.getBytes("newpass".toCharArray())
        def input2 = "nonaesmaster".toCharArray()

        def captured1 = new Capture<byte[]>()
        def captured2 = new Capture<char[]>()

        expect(em.encrypt(capture(captured1),capture(captured2)))
            .andReturn(new AESEncryptionData([0] as byte[],[0] as byte[], [0] as byte[]))

        replay(ds, em)

        def pwman = new PasswordManagerAES(ds)
        pwman.setEncryptionModel(em)


        pwman.updatePassword("def", input2, "newpass".toCharArray())

        assert captured.value.ref == "def"
        assert captured1.value == input1
        assert captured2.value == input2

    }

    void testDelete() {

        def ds = mock(Datasource)

        expect(ds.delPass(eq("def")))
            .andVoid()
            .andStubThrow(new RefNotFoundException())


        replay(ds)

        def pwman = new PasswordManagerAES(ds)

        pwman.deletePassword("def")

        shouldFail(RefNotFoundException) {

            pwman.deletePassword("def")

        }

    }


}
