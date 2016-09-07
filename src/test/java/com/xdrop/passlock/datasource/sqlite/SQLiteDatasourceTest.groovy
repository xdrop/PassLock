package com.xdrop.passlock.datasource.sqlite

import com.xdrop.passlock.search.FuzzySearcher
import org.easymock.EasyMock
import org.junit.Before
import org.junit.Test

class SQLiteDatasourceTest extends GroovyTestCase {

    def datasource = new SQLiteDatasource();


    @Before
    protected void setUp() {
        datasource.initialize()
        datasource.addPass("www.google.com", "googlepass");
        datasource.addPass("www.facebook.com", "facebookpass");
    }

    @Test
    void testGetPassFuzzy() {

        def fuzzySearcher = EasyMock.createMock(FuzzySearcher.class);

        EasyMock.expect(fuzzySearcher.search("google", ["www.google.com"])).andReturn("googlepass");
        EasyMock.replay(fuzzySearcher);

        assertEquals datasource.getPass("google", fuzzySearcher), "googlepass";
    }

    @Test
    void testGetPassExact() {
        assertEquals datasource.getPass("www.google.com"), "googlepass";
    }

    void testDelPass() {

        datasource.delPass("www.google.com");

        assertNull(datasource.getPass("www.google.com"));

    }

    void testUpdatePass() {

    }

    void testAddPass() {

        def encrypted = "b34njh234987sjhds76232h"

        datasource.addPass("www.bing.com", encrypted)

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
