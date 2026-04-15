package com.tdd.pricing.controller;

import com.tdd.pricing.api.model.BasketRequest;
import com.tdd.pricing.api.model.PriceResponse;
import com.tdd.pricing.mapper.BasketMapper;
import com.tdd.pricing.service.PricingService;
import com.tdd.pricing.domain.model.Basket;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    private final BasketMapper basketMapper;
    private final PricingService pricingService;

    @PostMapping
    public PriceResponse calculatePrice(@RequestBody BasketRequest request) {

        Basket basket = basketMapper.toDomain(request);

        double totalPrice = pricingService.calculatePrice(basket);

        PriceResponse response = new PriceResponse();
        response.setTotalPrice(totalPrice);

        return response;
    }

}
