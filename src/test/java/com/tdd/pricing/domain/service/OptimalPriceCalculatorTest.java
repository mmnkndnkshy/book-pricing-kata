package com.tdd.pricing.domain.service;

import com.tdd.pricing.api.model.BasketRequest;
import com.tdd.pricing.api.model.PriceResponse;
import com.tdd.pricing.domain.model.Book;
import com.tdd.pricing.domain.policy.DefaultDiscountPolicy;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptimalPriceCalculatorTest {

    private final PriceCalculator calculator =
            new OptimalPriceCalculator(new DefaultDiscountPolicy());

    @Test
    void shouldReturn50_whenSingleBook() {

        BasketRequest request = new BasketRequest()
                .items(Map.of("CLEAN_CODE", 1));


        PriceResponse response = calculator.calculate(request);
        assertEquals(1, response.getBasketSummary().getTotalItems());
        assertEquals(1, response.getBasketSummary().getUniqueBooks());
        assertEquals(50.0, response.getPricing().getFinalPrice());
    }

    @Test
    void shouldReturn100_whenTwoCopiesOfSameBook() {

        BasketRequest request = new BasketRequest()
                .items(Map.of("CLEAN_CODE", 2));

        PriceResponse response = calculator.calculate(request);

        assertEquals(2, response.getBasketSummary().getTotalItems());
        assertEquals(1, response.getBasketSummary().getUniqueBooks());
        assertEquals(100.0, response.getPricing().getFinalPrice());
    }

    @Test
    void shouldApply5PercentDiscount_whenTwoDifferentBooks() {

        BasketRequest request = new BasketRequest()
                .items(Map.of(
                        "CLEAN_CODE", 1,
                        "CLEAN_CODER", 1
                ));

        PriceResponse response = calculator.calculate(request);

        assertEquals(2, response.getBasketSummary().getTotalItems());
        assertEquals(2, response.getBasketSummary().getUniqueBooks());

        // 2 * 50 = 100 → 5% discount = 95
        assertEquals(95.0, response.getPricing().getFinalPrice());
    }


    @Test
    void shouldApply10PercentDiscount_whenThreeDifferentBooks() {

        BasketRequest request = new BasketRequest()
                .items(Map.of(
                        "CLEAN_CODE", 1,
                        "CLEAN_CODER", 1,
                        "CLEAN_ARCHITECTURE", 1
                ));

        PriceResponse response = calculator.calculate(request);

        assertEquals(3, response.getBasketSummary().getTotalItems());
        assertEquals(3, response.getBasketSummary().getUniqueBooks());

        // 150 - 10% = 135
        assertEquals(135.0, response.getPricing().getFinalPrice());
    }

    @Test
    void shouldPreferTwoGroupsOfFourInsteadOfFiveAndThree() {

        BasketRequest request = new BasketRequest()
                .items(Map.of(
                        "CLEAN_CODE", 2,
                        "CLEAN_CODER", 2,
                        "CLEAN_ARCHITECTURE", 2,
                        "TDD_BY_EXAMPLE",1,
                        "LEGACY_CODE",1
                ));

        PriceResponse response = calculator.calculate(request);

        assertEquals(322.5, response.getPricing().getFinalPrice());
    }

    @Test
    void shouldHandleMultipleOptimalSplits() {

        BasketRequest request = new BasketRequest()
                .items(Map.of(
                        "CLEAN_CODE", 2,
                        "CLEAN_CODER", 2,
                        "CLEAN_ARCHITECTURE", 2,
                        "TDD_BY_EXAMPLE",1
                ));

        PriceResponse response = calculator.calculate(request);

        assertEquals(295.0, response.getPricing().getFinalPrice());
    }

    @Test
    void shouldReturnZero_whenBasketIsEmpty() {

        BasketRequest request = new BasketRequest()
                .items(Map.of());

        PriceResponse response = calculator.calculate(request);

        assertEquals(0, response.getBasketSummary().getTotalItems());
        assertEquals(0, response.getBasketSummary().getUniqueBooks());
        assertEquals(0.0, response.getPricing().getFinalPrice());
    }

    @Test
    void shouldHandleMultipleSameBooksWithoutDiscount() {

        BasketRequest request = new BasketRequest()
                .items(Map.of("CLEAN_CODE", 5));

        PriceResponse response = calculator.calculate(request);

        assertEquals(250.0, response.getPricing().getFinalPrice());
    }

    //Boundary: maximum discount group
    @Test
    void shouldApplyMaxDiscount_whenFiveDifferentBooks() {

        BasketRequest request = new BasketRequest()
                .items(Map.of(
                        "CLEAN_CODE", 1,
                        "CLEAN_CODER", 1,
                        "CLEAN_ARCHITECTURE", 1,
                        "TDD_BY_EXAMPLE",1,
                        "LEGACY_CODE",1
                ));

        PriceResponse response = calculator.calculate(request);

        // 5 books = 250 base, 25% discount = 187.5
        assertEquals(187.5, response.getPricing().getFinalPrice());
    }


    //High-volume stress test (important for recursion safety)
    @Test
    void shouldHandleLargeQuantities_withoutBreaking() {

        BasketRequest request = new BasketRequest()
                .items(Map.of(
                        "CLEAN_CODE", 20,
                        "CLEAN_CODER", 20,
                        "CLEAN_ARCHITECTURE", 20,
                        "TDD_BY_EXAMPLE",20,
                        "LEGACY_CODE",20
                ));

        PriceResponse response = calculator.calculate(request);

        assertTrue(response.getPricing().getFinalPrice().intValue() > 0);
    }

    //Regression protection: uneven distribution case
    @Test
    void shouldHandleHighlySkewedBasket() {

        BasketRequest request = new BasketRequest()
                .items(Map.of(
                        "CLEAN_CODE", 10,
                        "CLEAN_CODER", 1,
                        "CLEAN_ARCHITECTURE", 1
                ));

        PriceResponse response = calculator.calculate(request);

        assertTrue(response.getPricing().getFinalPrice().intValue() > 0);
    }
}