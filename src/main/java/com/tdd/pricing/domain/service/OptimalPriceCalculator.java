package com.tdd.pricing.domain.service;

import com.tdd.pricing.api.model.*;
import com.tdd.pricing.domain.model.Book;
import com.tdd.pricing.domain.policy.DiscountPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tdd.pricing.domain.constant.PricingConstants.*;

@Component
@RequiredArgsConstructor
public class OptimalPriceCalculator implements PriceCalculator {

    private final DiscountPolicy discountPolicy;

    @Override
    public PriceResponse calculate(BasketRequest request) {

        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            return emptyResponse();
        }

        Map<Book, Integer> items = request.getItems().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> Book.valueOf(e.getKey()),
                        Map.Entry::getValue
                ));

        int totalItems = items.values().stream().mapToInt(Integer::intValue).sum();
        int uniqueBooks = items.size();

        double basePrice = totalItems * UNIT_BOOK_PRICE;

        double finalPrice = computeTotalPrice(new HashMap<>(items));

        double discountAmount = basePrice - finalPrice;

        return new PriceResponse()
                .basketSummary(new BasketSummary()
                        .totalItems(totalItems)
                        .uniqueBooks(uniqueBooks)
                )
                .pricing(new Pricing()
                        .basePrice(basePrice)
                        .discountAmount(discountAmount)
                        .finalPrice(finalPrice)
                );
    }

    private double computeTotalPrice(Map<Book, Integer> items) {

        if (items.values().stream().allMatch(qty -> qty == ZERO)) {
            return ZERO_PRICE;
        }

        int groupSize = (int) items.values().stream()
                .filter(qty -> qty > ZERO)
                .count();

        double groupPrice = groupSize * UNIT_BOOK_PRICE *
                (ONE - discountPolicy.getDiscount(groupSize));

        Map<Book, Integer> reduced = items.entrySet().stream()
                .collect(HashMap::new,
                        (map, e) -> map.put(
                                e.getKey(),
                                e.getValue() > ZERO ? e.getValue() - ONE : ZERO
                        ),
                        HashMap::putAll
                );

        return groupPrice + computeTotalPrice(reduced);
    }

    private PriceResponse emptyResponse() {
        return new PriceResponse()
                .basketSummary(new BasketSummary()
                        .totalItems(ZERO)
                        .uniqueBooks(ZERO)
                )
                .pricing(new Pricing()
                        .basePrice(ZERO_PRICE)
                        .discountAmount(ZERO_PRICE)
                        .finalPrice(ZERO_PRICE)
                );
    }
}