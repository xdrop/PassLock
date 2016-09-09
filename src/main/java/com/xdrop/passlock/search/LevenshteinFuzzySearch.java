package com.xdrop.passlock.search;

import com.xdrop.passlock.model.FuzzySearchResult;
import com.xdrop.passlock.utils.Optional;

import java.util.List;

public class LevenshteinFuzzySearch implements FuzzySearcher {


    @Override
    public List<FuzzySearchResult> search(String ref, List<String> options) {
        return null;
    }

    @Override
    public Optional<FuzzySearchResult> searchTop(String ref, List<String> options) {
        return null;
    }
}
