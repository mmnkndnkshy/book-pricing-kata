package com.tdd.pricing.domain.model;

import java.util.Map;

public record BasketKey(Map<Book, Integer> items) {
    public BasketKey{
        items = Map.copyOf(items);
    }
}
