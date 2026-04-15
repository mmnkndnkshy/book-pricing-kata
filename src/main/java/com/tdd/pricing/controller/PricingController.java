package com.tdd.pricing.controller;

import com.tdd.pricing.api.model.BasketRequest;
import com.tdd.pricing.api.model.PriceResponse;
import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.model.PricingResult;
import com.tdd.pricing.mapper.BasketMapper;
import com.tdd.pricing.mapper.PricingMapper;
import com.tdd.pricing.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pricing/v1")
public class PricingController {

    private final BasketMapper basketMapper;
    private final PricingService pricingService;
    private final PricingMapper pricingMapper;

    @PostMapping("/calculatePrice")
    public PriceResponse calculatePrice(@RequestBody BasketRequest request) {

        Basket basket = basketMapper.toDomain(request);

        PricingResult result = pricingService.calculate(basket);

        return pricingMapper.toResponse(result);
    }
}