package com.tdd.pricing.domain.policy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultDiscountPolicy implements DiscountPolicy {
    private static final Map<Integer, Double> DISCOUNTS = Map.of(
            1, 0.0,
            2, 0.05,
            3, 0.10,
            4, 0.20,
            5, 0.25
    );

    @Override
    public double getDiscount(int distinctBookCount) {
        return DISCOUNTS.getOrDefault(distinctBookCount, 0.0);
    }
}
