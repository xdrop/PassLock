package com.xdrop.passlock.crypto.aes

import com.xdrop.passlock.utils.ByteUtils
import com.xdrop.passlock.utils.KeyUtils
import org.junit.Before

class AESEncryptDecryptTest extends GroovyTestCase {

    def SECRET_KEY = "7xqBQI1RrRWczoLlS++26Q==";
    def SALT = "uzdvIH/Ithk=";

    def secretKey = KeyUtils.stringToAESSecretKey(SECRET_KEY)
    def encrypted = "encryptme";
    AESEncryptDecrypt instance;

    @Before
    void setUp() {
        super.setUp()
        instance = new AESEncryptDecrypt();
    }

    void testEncrypt() {

        def encryptionData = instance.encrypt(encrypted.getBytes("UTF-8"), secretKey)
        def encString = ByteUtils.toBase64(encryptionData.encryptedPayload)

        assertNotNull encString
        assertNotNull encryptionData.initilizationVector

        assert instance.decrypt(encryptionData, secretKey) == encrypted.getBytes("UTF-8")

    }

    void testDecrypt() {

        def encrypted = "Fmi5Sm5w1rfpXFLvOLOSJA=="
        def iv = "Z+sO7nqJ+UYUCrHlCh3v9g==";

        def aes = new AESEncryptionData();
        aes.encryptedPayload = ByteUtils.fromBase64(encrypted)
        aes.initilizationVector = ByteUtils.fromBase64(iv)
        aes.salt = ByteUtils.fromBase64(SALT)

        def output = instance.decrypt(aes, secretKey)

        assertEquals new String(output), "encryptme"

    }
}
