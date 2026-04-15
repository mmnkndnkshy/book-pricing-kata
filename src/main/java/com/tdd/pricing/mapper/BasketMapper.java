package com.tdd.pricing.mapper;

import com.tdd.pricing.api.model.BasketRequest;
import com.tdd.pricing.domain.model.Basket;
import com.tdd.pricing.domain.model.Book;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BasketMapper {

    public Basket toDomain(BasketRequest request) {

        Map<Book, Integer> items = request.getItems()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> Book.valueOf(e.getKey()),
                        Map.Entry::getValue
                ));

        return new Basket(items);
    }
}