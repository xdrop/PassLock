package com.xdrop.passlock.search

import org.codehaus.groovy.transform.LogASTTransformation

class LevenshteinFuzzySearchTDD extends GroovyTestCase {

    def search = new LevenshteinFuzzySearch()
    List options;

    void setUp() {
        super.setUp()

        options = ["www.google.com", "www.irrelevant.com", "www.bing.com", "www.github.com", "gmail.com"]

    }

    void testSearch() {

        assertEquals search.search("google", options), "www.google.com"

    }
}
