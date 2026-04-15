package com.tdd.pricing.domain.model;

import lombok.Value;

import java.util.Map;

@Value
public class Basket {
    Map<Book, Integer> items;
}