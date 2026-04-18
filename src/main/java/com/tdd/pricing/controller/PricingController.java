package com.tdd.pricing.controller;

import com.tdd.pricing.api.model.BasketRequest;
import com.tdd.pricing.api.model.PriceResponse;
import com.tdd.pricing.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pricing/v1")
public class PricingController {

    private final PricingService pricingService;

    @PostMapping("/calculatePrice")
    public PriceResponse calculatePrice(@RequestBody BasketRequest request) {
        return pricingService.calculate(request);
    }
}