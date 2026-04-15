package com.tdd.pricing.service;

import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.model.PricingResult;
import com.tdd.pricing.domain.service.PriceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final PriceCalculator priceCalculator;

    public PricingResult calculate(Basket basket) {
        return priceCalculator.calculate(basket);
    }
}