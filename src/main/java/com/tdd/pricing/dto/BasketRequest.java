package com.tdd.pricing.dto;

import com.tdd.pricing.domain.model.Book;

import java.util.Map;
// Request DTO
public record BasketRequest(Map<Book,Integer> items) {
}
