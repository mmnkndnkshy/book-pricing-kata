package com.tdd.pricing.controller;

import com.tdd.pricing.service.PricingService;
import com.tdd.pricing.domain.model.Basket;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping
    public PriceResponse calculatePrice(@RequestBody BasketRequest request) {
        Basket basket   = new Basket(request.items());
        double totalPrice = pricingService.calculatePrice(basket);
        return new PriceResponse(totalPrice);
    }

}
