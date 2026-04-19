package com.tdd.pricing.domain.service;

import com.tdd.pricing.api.model.BasketRequest;
import com.tdd.pricing.api.model.PriceResponse;

public interface PriceCalculator {

    PriceResponse calculate(BasketRequest request);
}