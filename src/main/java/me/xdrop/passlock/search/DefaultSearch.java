package me.xdrop.passlock.search;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import me.xdrop.passlock.utils.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultSearch implements FuzzySearcher {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultSearch.class);
    private int certainMatch = 94;
    private int cutoff = 40;

    public DefaultSearch(int certainMatch, int cutoff) {
        this.certainMatch = certainMatch;
        this.cutoff = cutoff;
    }

    public DefaultSearch() {
    }

    @Override
    public List<String> search(String ref, List<String> options) {

        return search(ref, options, -1);

    }

    @Override
    public List<String> search(String ref, List<String> options, int limit) {

        List<String> results = new ArrayList<>();
        List<ExtractedResult> extractedResults;

        LOG.debug("Searching for " + ref + " amongst a list of " + options.size() + " elements...");

        if (limit > 0) {
            extractedResults = FuzzySearch.extractTop(ref, options, limit, cutoff);
        } else {
            extractedResults = FuzzySearch.extractSorted(ref, options, cutoff);
        }

        if(extractedResults.size() != 0){

            ExtractedResult top = extractedResults.get(0);

            if(top.getScore() >= certainMatch) {

                LOG.debug("Certain match encountered: " + top.getString());

                results.add(top.getString());
                return results;

            }

        }


        for (ExtractedResult extractedResult : extractedResults){

            results.add(extractedResult.getString());

        }

        LOG.debug(results.size() + " results found");

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
