package com.tdd.pricing.domain.service;

import com.tdd.pricing.domain.model.Basket;
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
    void shouldPreferTwoGroupsOfFourInsteadOfFiveAndThree() {

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

    @Test
    void shouldHandleMultipleOptimalSplits() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 2,
                Book.CLEAN_CODER, 2,
                Book.CLEAN_ARCHITECTURE, 1,
                Book.TDD_BY_EXAMPLE, 1
        ));

        double result = calculator.calculatePrice(basket);

        // Expected optimal grouping: 4 + 2
        // 4 → 200 - 20% = 160
        // 2 → 100 - 5% = 95
        // Total = 255
        assertEquals(255.0, result);
    }

    @Test
    void shouldReturnZero_whenBasketIsEmpty() {

        Basket basket = new Basket(Map.of());

        double result = calculator.calculatePrice(basket);

        assertEquals(0.0, result);
    }

    @Test
    void shouldHandleMultipleSameBooksWithoutDiscount() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 5
        ));

        double result = calculator.calculatePrice(basket);

        assertEquals(250.0, result);
    }

    //Boundary: maximum discount group
    @Test
    void shouldApplyMaxDiscount_whenFiveDifferentBooks() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 1,
                Book.CLEAN_CODER, 1,
                Book.CLEAN_ARCHITECTURE, 1,
                Book.TDD_BY_EXAMPLE, 1,
                Book.LEGACY_CODE, 1
        ));

        double result = calculator.calculatePrice(basket);

        // 5 books = 250 base, 25% discount = 187.5
        assertEquals(187.5, result);
    }

    //Edge case: single copy of each book type
    @Test
    void shouldHandleAllBooksOnce() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 1,
                Book.CLEAN_CODER, 1,
                Book.CLEAN_ARCHITECTURE, 1,
                Book.TDD_BY_EXAMPLE, 1,
                Book.LEGACY_CODE, 1
        ));

        double result = calculator.calculatePrice(basket);

        assertTrue(result > 0);
        assertEquals(187.5, result);
    }

    //High-volume stress test (important for recursion safety)
    @Test
    void shouldHandleLargeQuantities_withoutBreaking() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 20,
                Book.CLEAN_CODER, 20,
                Book.CLEAN_ARCHITECTURE, 20,
                Book.TDD_BY_EXAMPLE, 20,
                Book.LEGACY_CODE, 20
        ));

        double result = calculator.calculatePrice(basket);

        assertTrue(result > 0);
    }

    //Regression protection: uneven distribution case
    @Test
    void shouldHandleHighlySkewedBasket() {

        Basket basket = new Basket(Map.of(
                Book.CLEAN_CODE, 10,
                Book.CLEAN_CODER, 1,
                Book.CLEAN_ARCHITECTURE, 1
        ));

        double result = calculator.calculatePrice(basket);

        assertTrue(result > 0);
    }
}