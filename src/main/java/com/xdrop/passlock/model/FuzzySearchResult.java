package com.xdrop.passlock.model;

public class FuzzySearchResult implements Comparable<FuzzySearchResult>{

    private String reference;

    private int weight;

    public FuzzySearchResult(String reference, int weight) {
        this.reference = reference;
        this.weight = weight;
    }

    @Override
    public int compareTo(FuzzySearchResult o) {
        return Double.compare(o.getWeight(),getWeight());
    }

    public String getReference() {
        return reference;
    }

    public int getWeight() {
        return weight;
    }
}
