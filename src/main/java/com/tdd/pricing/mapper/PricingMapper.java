package com.tdd.pricing.mapper;

import com.tdd.pricing.api.model.*;
import com.tdd.pricing.domain.model.PricingResult;
import org.springframework.stereotype.Component;

@Component
public class PricingMapper {

    public PriceResponse toResponse(PricingResult result) {

        BasketSummary basketSummary = new BasketSummary();
        basketSummary.setTotalItems(result.getTotalItems());
        basketSummary.setUniqueBooks(result.getUniqueBooks());

        Pricing pricing = new Pricing();
        pricing.setBasePrice(result.getBasePrice());
        pricing.setDiscountAmount(result.getDiscountAmount());
        pricing.setFinalPrice(result.getFinalPrice());

        PriceResponse response = new PriceResponse();
        response.setBasketSummary(basketSummary);
        response.setPricing(pricing);

        return response;
    }
}