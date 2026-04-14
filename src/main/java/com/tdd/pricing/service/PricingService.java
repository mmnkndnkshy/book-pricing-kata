package com.tdd.pricing.application;

import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.policy.DefaultDiscountPolicy;
import com.tdd.pricing.domain.service.OptimalPriceCalculator;
import com.tdd.pricing.domain.service.PriceCalculator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PricingService {
    private PriceCalculator priceCalculator;
    public PricingService(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }
    @Cacheable(value ="basketPriceCache",key ="#root.args[0].getItems()")
    public double calculatePrice(Basket basket) {
        return priceCalculator.calculatePrice(basket);
    }
}
