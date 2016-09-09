package com.xdrop.passlock.model;

public class FuzzySearchResult implements Comparable<FuzzySearchResult>{

    private String reference;

    private double weight;


    @Override
    public int compareTo(FuzzySearchResult o) {
        return Double.compare(getWeight(), o.getWeight());
    }

    public String getReference() {
        return reference;
    }

    public double getWeight() {
        return weight;
    }
}
