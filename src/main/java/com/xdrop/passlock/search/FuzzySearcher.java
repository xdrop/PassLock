package com.xdrop.passlock.search;

import java.util.List;

public interface FuzzySearcher {

    String search(String ref, List<String> options);

}
