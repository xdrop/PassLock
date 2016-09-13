package com.xdrop.passlock.search

import com.xdrop.passlock.LogGroovyTestCase

class FuzzyWuzzySearchTDD extends LogGroovyTestCase {

    def search = new FuzzyWuzzySearch()
    List options;

    void setUp() {
        super.setUp()

        options = ["www.google.com", "www.shouldntmatter.com", "www.bing.com",
                   "www.github.com", "gmail.com", "food.com", "bbc.com",
                   "www.bbc.com", "www.veryinterestingnews.com", "www.malted.com",
                   "www.mavencentral.com", "www.twitter.com", "mysocialmedia",
                   "laptoppass"]

    }

    void testSearch() {

        assertEquals "www.bing.com", search.search("bng",options).get(0).getReference()
        assertEquals "www.google.com", search.search("google", options).get(0).getReference()
        assertEquals "www.github.com", search.search("git hub",options).get(0).getReference()
        assertEquals "gmail.com", search.search("email",options).get(0).getReference()
        assertEquals "www.github.com", search.search("git hub",options).get(0).getReference()
        assertEquals "www.github.com", search.search("git hub",options).get(0).getReference()
        assertEquals "laptoppass", search.search("lptp", options).get(0).getReference()
        assertEquals "www.mavencentral.com", search.search("mvn", options).get(0).getReference()


    }

    void testSearchSingle() {

        assertEquals "www.bing.com", search.searchTop("bng", options).get()
        assertEquals "www.google.com", search.searchTop("google", options).get()
        assertEquals "laptoppass", search.searchTop("lptp", options).get()
        assertEquals "www.mavencentral.com", search.searchTop("mvn", options).get()

        assert !search.searchTop("asbdfdfdsfsdf", options).isPresent()

    }
}
