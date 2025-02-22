package com.company.web.springdemo.models;

import java.util.Optional;

public class FilterOptions {

    private Optional<String> name;
    private Optional<Double> minAbv;
    private Optional<Double> maxAbv;
    private Optional<Integer> styleId;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions() {
        this(null, null, null, null, null, null);
    }

    public FilterOptions(
            String name,
            Double minAbv,
            Double maxAbv,
            Integer styleId,
            String sortBy,
            String sortOrder) {
        this.name = Optional.ofNullable(name);
        this.minAbv = Optional.ofNullable(minAbv);
        this.maxAbv = Optional.ofNullable(maxAbv);
        this.styleId = Optional.ofNullable(styleId);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<Double> getMinAbv() {
        return minAbv;
    }

    public Optional<Double> getMaxAbv() {
        return maxAbv;
    }

    public Optional<Integer> getStyleId() {
        return styleId;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

}
