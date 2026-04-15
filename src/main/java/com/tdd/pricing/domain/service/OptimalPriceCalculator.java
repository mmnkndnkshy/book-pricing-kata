package com.tdd.pricing.domain.service;

import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.model.Book;
import com.tdd.pricing.domain.policy.DiscountPolicy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class OptimalPriceCalculator implements PriceCalculator {

    private static final double BOOK_PRICE = 50.0;
    private static final double ZERO_PRICE = 0.0;

    private final DiscountPolicy discountPolicy;
    private final Map<String, Double> memo = new HashMap<>();

    public OptimalPriceCalculator(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Override
    public double calculatePrice(Basket basket) {
        return calculate(toCounts(basket));
    }

    // Java 17 style conversion
    private int[] toCounts(Basket basket) {
        return Arrays.stream(Book.values())
                .mapToInt(book -> basket.getItems().getOrDefault(book, 0))
                .toArray();
    }

    private double calculate(int[] counts) {

        if (isEmpty(counts)) return ZERO_PRICE;

        String key = keyOf(counts);

        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        double minPrice = possibleGroupSizes(counts).stream()
                .mapToDouble(size -> calculateForGroupSize(counts, size))
                .min()
                .orElse(Double.POSITIVE_INFINITY);

        double finalPrice = (minPrice == Double.POSITIVE_INFINITY)
                ? ZERO_PRICE
                : minPrice;

        memo.put(key, finalPrice);
        return finalPrice;
    }

    // Core improvement: try ALL combinations
    private double calculateForGroupSize(int[] counts, int size) {

        List<int[]> combinations = generateCombinations(counts, size);

        return combinations.stream()
                .mapToDouble(newCounts -> priceFor(size) + calculate(newCounts))
                .min()
                .orElse(Double.POSITIVE_INFINITY);
    }

    // Generate all valid combinations
    private List<int[]> generateCombinations(int[] counts, int size) {
        List<int[]> result = new ArrayList<>();
        backtrack(counts, size, 0, new ArrayList<>(), result);
        return result;
    }

    // Backtracking
    private void backtrack(int[] counts,
                           int size,
                           int start,
                           List<Integer> current,
                           List<int[]> result) {

        if (current.size() == size) {
            result.add(applyCombination(counts, current));
            return;
        }

        for (int i = start; i < counts.length; i++) {
            if (counts[i] > 0) {
                current.add(i);
                backtrack(counts, size, i + 1, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    // Apply selected combination
    private int[] applyCombination(int[] counts, List<Integer> indices) {
        int[] newCounts = counts.clone();
        for (int index : indices) {
            newCounts[index]--;
        }
        return newCounts;
    }


    // Helper methods

    private boolean isEmpty(int[] counts) {
        return Arrays.stream(counts).allMatch(c -> c == 0);
    }

    private String keyOf(int[] counts) {
        return Arrays.toString(counts);
    }

    private List<Integer> possibleGroupSizes(int[] counts) {
        int max = (int) Arrays.stream(counts)
                .filter(c -> c > 0)
                .count();

        return IntStream.rangeClosed(1, max)
                .boxed()
                .toList();
    }

    private double priceFor(int size) {
        double discount = discountPolicy.getDiscount(size);
        return size * BOOK_PRICE * (1 - discount);
    }

}