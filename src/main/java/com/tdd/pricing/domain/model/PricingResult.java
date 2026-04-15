package com.tdd.pricing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PricingResult {

    private final int totalItems;
    private final int uniqueBooks;

    private final double basePrice;
    private final double discountAmount;
    private final double finalPrice;
}
