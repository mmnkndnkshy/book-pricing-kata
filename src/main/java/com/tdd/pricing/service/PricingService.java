package com.tdd.pricing.service;

import com.tdd.pricing.api.model.BasketRequest;
import com.tdd.pricing.api.model.PriceResponse;
import com.tdd.pricing.domain.service.PriceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final PriceCalculator calculator;

    public PriceResponse calculate(BasketRequest request) {
        return calculator.calculate(request);
    }
}