package com.dt;

import java.util.List;

public class DividedSet {
    private List<List<String>> matched;
    private List<List<String>> unmatched;

    public DividedSet() {

    }

    public List<List<String>> getMatched() {
        return matched;
    }

    public void setMatched(List<List<String>> matched) {
        this.matched = matched;
    }

    public List<List<String>> getUnmatched() {
        return unmatched;
    }

    public void setUnmatched(List<List<String>> unmatched) {
        this.unmatched = unmatched;
    }
}
