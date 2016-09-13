package com.xdrop.passlock.search;

import com.xdrop.fuzzywuzzy.FuzzySearch;
import com.xdrop.fuzzywuzzy.model.ExtractedResult;
import com.xdrop.passlock.model.FuzzySearchResult;
import com.xdrop.passlock.utils.Optional;

import java.util.ArrayList;
import java.util.List;

public class FuzzyWuzzySearch implements FuzzySearcher {

    @Override
    public List<FuzzySearchResult> search(String ref, List<String> options) {

        List<FuzzySearchResult> results = new ArrayList<>();

        for (ExtractedResult extractedResult : FuzzySearch.extractTop(ref, options, 5)){

            results.add(new FuzzySearchResult(extractedResult.getString(), extractedResult.getScore()));

        }

        return results;

    }

    @Override
    public Optional<String> searchTop(String ref, List<String> options) {

        ExtractedResult res = FuzzySearch.extractOne(ref, options);

        if(res.getScore() > 40) {
            return Optional.of(res.getString());
        } else {
            return Optional.empty();
        }
    }
}
