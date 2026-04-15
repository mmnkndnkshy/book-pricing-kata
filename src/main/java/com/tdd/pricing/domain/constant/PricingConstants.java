package com.tdd.pricing.domain.constant;

import java.util.Map;

public final class PricingConstants {

    private PricingConstants() {
        // prevent instantiation
    }

    public static final double BOOK_PRICE = 50.0;
    public static final double ZERO_PRICE = 0.0;
    public static final int ZERO = 0;
    public static final int ONE = 1;

    public static final Map<Integer, Double> DISCOUNTS = Map.of(
            1, 0.0,
            2, 0.05,
            3, 0.10,
            4, 0.20,
            5, 0.25
    );
}