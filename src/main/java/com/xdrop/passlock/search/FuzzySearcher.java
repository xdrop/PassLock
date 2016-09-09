package com.xdrop.passlock.search;

import com.xdrop.passlock.model.FuzzySearchResult;
import com.xdrop.passlock.utils.Optional;

import java.util.List;

public interface FuzzySearcher {

    List<FuzzySearchResult> search(String ref, List<String> options);

    Optional<FuzzySearchResult> searchTop(String ref, List<String> options);

}
