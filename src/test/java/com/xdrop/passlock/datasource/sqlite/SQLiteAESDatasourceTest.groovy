package com.xdrop.passlock.datasource.sqlite

import com.xdrop.passlock.crypto.aes.AESEncryptionData
import com.xdrop.passlock.model.EncryptionData
import com.xdrop.passlock.model.PasswordEntry
import com.xdrop.passlock.search.FuzzySearcher
import org.easymock.EasyMock
import org.junit.Before
import org.junit.Test

class SQLiteAESDatasourceTest extends GroovyTestCase {

    def datasource = new SQLiteAESDatasource();


    @Before
    protected void setUp() {
        datasource.initialize()
        def passwordEntry = new PasswordEntry<AESEncryptionData>();
        passwordEntry.ref = "www.bing.com"
        passwordEntry.description = "description example"
        datasource.addPass("www.bing.com", passwordEntry)
        datasource.addPass("www.google.com", passwordEntry)
        datasource.addPass("www.facebook.com", passwordEntry)
    }

    @Test
    void testGetPassFuzzy() {

        def fuzzySearcher = EasyMock.createMock(FuzzySearcher.class);

        EasyMock.expect(fuzzySearcher.search("google", ["www.google.com"])).andReturn("googlepass");
        EasyMock.replay(fuzzySearcher);

        def passentry = datasource.getPass("google", fuzzySearcher)

        assertNotNull passentry

    }

    @Test
    void testGetPassExact() {
        assertEquals "googlepass".toCharArray(), datasource.getPass("www.google.com");
    }

    void testDelPass() {

        datasource.delPass("www.google.com");

        assertNull(datasource.getPass("www.google.com"));

    }

    void testUpdatePass() {

    }

    void testAddPass() {

        def encrypted = "b34njh234987sjhds76232h"

        def passwordEntry = new PasswordEntry<AESEncryptionData>();
        passwordEntry.ref = "www.bing.com"
        passwordEntry.description = "description example"
        datasource.addPass("www.bing.com", passwordEntry)

        assertEquals datasource.getPass("www.bing.com"), passwordEntry

    }

    void testAddPass1() {

        def desc = "Test description"

        datasource.addPass("www.bing.com", "...", desc)

        assertNotNull datasource.getPass("www.bing.com")
        assertEquals datasource.getPass("www.bing.com").getDescription(), desc

    }

    void testInitialize() {

        datasource.initialize()

        def file = new File("store.db")
        assertTrue(file.exists());

    }
}
