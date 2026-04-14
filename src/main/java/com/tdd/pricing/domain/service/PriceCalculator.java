package com.tdd.pricing.domain.service;

import com.tdd.pricing.domain.model.Basket;

public interface PriceCalculator {
    double calculatePrice(Basket basket);
}
