package com.xdrop.passlock.datasource.sqlite

import com.xdrop.passlock.search.FuzzySearcher
import org.easymock.EasyMock
import org.junit.Before
import org.junit.Test

class SQLiteAESDatasourceTest extends GroovyTestCase {

    def datasource = new SQLiteAESDatasource();


    @Before
    protected void setUp() {
        datasource.initialize()
        datasource.addPass("www.google.com");
        datasource.addPass("www.facebook.com");
    }

    @Test
    void testGetPassFuzzy() {

        def fuzzySearcher = EasyMock.createMock(FuzzySearcher.class);

        EasyMock.expect(fuzzySearcher.search("google", ["www.google.com"])).andReturn("googlepass");
        EasyMock.replay(fuzzySearcher);

        assertEquals datasource.getPass("google", fuzzySearcher).encryptionData.encryptedPayload, "googlepass".toCharArray();
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

        datasource.addPass("www.bing.com")

        assertEquals datasource.getPass("www.bing.com").password, encrypted.toCharArray()

    }

    void testAddPass1() {

        def desc = "Test description"

        datasource.addPass("www.bing.com", "...", desc)

        assertEquals datasource.getPass("www.bing.com").getDescription(), desc

    }

    void testInitialize() {

        datasource.initialize()

        def file = new File("store.db")
        assertTrue(file.exists());

    }
}
