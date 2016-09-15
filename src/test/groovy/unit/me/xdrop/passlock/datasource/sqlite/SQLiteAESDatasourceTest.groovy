package me.xdrop.passlock.datasource.sqlite

import me.xdrop.passlock.LogGroovyTestCase
import me.xdrop.passlock.PassLock
import me.xdrop.passlock.crypto.aes.AESEncryptionData
import me.xdrop.passlock.exceptions.RefNotFoundException
import me.xdrop.passlock.model.PasswordEntry
import me.xdrop.passlock.utils.ByteUtils
import org.apache.log4j.PropertyConfigurator
import org.junit.Before
import org.junit.Test

class SQLiteAESDatasourceTest extends LogGroovyTestCase {

    def datasource = new SQLiteAESDatasource();

    @Before
    protected void setUp() {

        PropertyConfigurator.configure(PassLock.loadPropertiesFile("log.properties"));

        datasource.reset()

        datasource.initialize()

        def passwordEntry = new PasswordEntry<AESEncryptionData>();

        passwordEntry.ref = "www.google.com"
        passwordEntry.description = "description example"

        passwordEntry.encryptionData = new AESEncryptionData();
        passwordEntry.encryptionData.initilizationVector = ByteUtils.fromBase64("iv==")
        passwordEntry.encryptionData.salt = ByteUtils.fromBase64("salt==")
        passwordEntry.encryptionData.encryptedPayload = ByteUtils.fromBase64("enc==")

        datasource.addPass("www.google.com", passwordEntry)

    }

    @Test
    void testGetPassExact() {

        def result = datasource.getPass("www.google.com")

        assertNotNull result

        assertEquals result.description, "description example"
        assertEquals result.ref, "www.google.com"
        assertEquals result.encryptionData.encryptedPayload, ByteUtils.fromBase64("enc==")
        assertEquals result.encryptionData.salt, ByteUtils.fromBase64("salt==")
        assertEquals result.encryptionData.initilizationVector, ByteUtils.fromBase64("iv==")

        shouldFail(RefNotFoundException){
            datasource.getPass("www.nonexistent.com")
        }

    }

    void testDelPass() {

        datasource.delPass("www.google.com");

        shouldFail(RefNotFoundException){
            datasource.getPass("www.google.com")
        }


    }

    void testUpdatePass() {

        def passwordEntry = dummyPasswordEntry("www.google.com", "updated example")

        datasource.updatePass("www.google.com", passwordEntry)

        def pass = datasource.getPass("www.google.com")

        assertNotNull pass
        assertEquals pass.description, "updated example"

        shouldFail(RefNotFoundException){
            datasource.updatePass("www.nonexistent.com", passwordEntry)
        }


    }

    void testAddPass() {

        PasswordEntry<AESEncryptionData> passwordEntry = dummyPasswordEntry("www.new.com", "description example")

        datasource.addPass("www.new.com", passwordEntry)

        def get = datasource.getPass("www.new.com")

        assertNotNull get

        assertEquals get.ref, "www.new.com"
        assertEquals get.description, "description example"

    }

    private static PasswordEntry<AESEncryptionData> dummyPasswordEntry(String ref, String desc) {
        def passwordEntry = new PasswordEntry<AESEncryptionData>();

        passwordEntry.ref = ref
        passwordEntry.description = desc

        passwordEntry.encryptionData = new AESEncryptionData();
        passwordEntry.encryptionData.initilizationVector = ByteUtils.fromBase64("iv==")
        passwordEntry.encryptionData.salt = ByteUtils.fromBase64("salt==")
        passwordEntry.encryptionData.encryptedPayload = ByteUtils.fromBase64("enc==")
        passwordEntry
    }

    void testInitialize() {

        datasource.initialize()

        def file = new File("store.db")
        assertTrue(file.exists());

    }
}
