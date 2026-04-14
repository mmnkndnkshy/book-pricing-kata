package com.tdd.pricing.domain.policy;

public interface DiscountPolicy {
    double getDiscount(int distinctBookCount);
}
