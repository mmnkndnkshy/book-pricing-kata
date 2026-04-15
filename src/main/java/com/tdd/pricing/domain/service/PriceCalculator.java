package com.tdd.pricing.domain.service;

import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.model.PricingResult;

public interface PriceCalculator {
    PricingResult calculate(Basket basket);
}
