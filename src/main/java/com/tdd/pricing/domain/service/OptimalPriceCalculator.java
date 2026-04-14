package com.tdd.pricing.domain.service;

import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.model.Book;
import com.tdd.pricing.domain.policy.DiscountPolicy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OptimalPriceCalculator implements PriceCalculator {

    private static final double BOOK_PRICE = 50.0;

    private final DiscountPolicy discountPolicy;
    private final Map<String, Double> memo = new HashMap<>();

    public OptimalPriceCalculator(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Override
    public double calculatePrice(Basket basket) {
        int[] counts = convertToCounts(basket);
        return calculate(counts);
    }

    private int[] convertToCounts(Basket basket) {
        int[] counts = new int[Book.values().length];

        for (Book book : Book.values()) {
            counts[book.ordinal()] = basket.getItems().getOrDefault(book, 0);
        }

        return counts;
    }

    private double calculate(int[] counts) {


        if (Arrays.stream(counts).allMatch(c -> c == 0)) {
            return 0.0;
        }

        String key = Arrays.toString(counts);

        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        double minPrice = Double.POSITIVE_INFINITY;

        int maxPossibleSize = (int) Arrays.stream(counts)
                .filter(c -> c > 0)
                .count();

        for (int size = 1; size <= maxPossibleSize; size++) {

            int[] newCounts = counts.clone();
            int taken = 0;

            for (int i = 0; i < newCounts.length && taken < size; i++) {
                if (newCounts[i] > 0) {
                    newCounts[i]--;
                    taken++;
                }
            }

            if (taken != size) {
                continue;
            }

            double discount = discountPolicy.getDiscount(size);
            double groupPrice = size * BOOK_PRICE * (1 - discount);


            double totalPrice = groupPrice + calculate(newCounts);

            minPrice = Math.min(minPrice, totalPrice);
        }

        if (minPrice == Double.POSITIVE_INFINITY) {
            return 0.0;
        }

        memo.put(key, minPrice);
        return minPrice;
    }
}