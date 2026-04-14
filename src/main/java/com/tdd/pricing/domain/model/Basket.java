package com.tdd.pricing.domain.model;

import java.util.Map;

public class Basket {

    private final Map<Book, Integer> items;

    public Basket(Map<Book, Integer> items) {
        this.items = items;
    }

    public Map<Book, Integer> getItems() {
        return items;
    }
}
