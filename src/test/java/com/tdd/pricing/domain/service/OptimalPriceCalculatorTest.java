package com.tdd.pricing.domain.service;

import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.model.Book;
import com.tdd.pricing.domain.policy.DefaultDiscountPolicy;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimalPriceCalculatorTest {

    private final PriceCalculator calculator =
            new OptimalPriceCalculator(new DefaultDiscountPolicy());

    @Test
    void shouldReturn50_whenSingleBook() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 1
        ));

        double result = calculator.calculatePrice(basket);

        assertEquals(50.0, result);
    }

    @Test
    void shouldReturn100_whenTwoCopiesOfSameBook() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 2
        ));

        double result = calculator.calculatePrice(basket);

        assertEquals(100.0, result);
    }


    @Test
    void shouldApply10PercentDiscount_whenThreeDifferentBooks() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 1,
                Book.CLEAN_CODER, 1,
                Book.CLEAN_ARCHITECTURE, 1
        ));

        double result = calculator.calculatePrice(basket);

        // 150 - 10% = 135
        assertEquals(135.0, result);
    }

    @Test
    void shouldApply5PercentDiscount_whenTwoDifferentBooks() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 1,
                Book.CLEAN_CODER, 1
        ));

        double result = calculator.calculatePrice(basket);

        // 2 * 50 = 100 → 5% discount = 95
        assertEquals(95.0, result);
    }

    @Test
    void shouldReturn320_whenComplexBasket() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 2,
                Book.CLEAN_CODER, 2,
                Book.CLEAN_ARCHITECTURE, 2,
                Book.TDD_BY_EXAMPLE, 1,
                Book.LEGACY_CODE, 1
        ));

        double result = calculator.calculatePrice(basket);

        assertEquals(320.0, result);
    }
}