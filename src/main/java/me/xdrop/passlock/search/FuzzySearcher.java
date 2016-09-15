package me.xdrop.passlock.search;

import me.xdrop.passlock.utils.Optional;

import java.util.List;

public interface FuzzySearcher {

    List<String> search(String ref, List<String> options);

    List<String> search(String ref, List<String> options, int limit);

    Optional<String> searchTop(String ref, List<String> options);

}
