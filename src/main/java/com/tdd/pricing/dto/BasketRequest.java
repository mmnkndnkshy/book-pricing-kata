package com.tdd.pricing.controller;

import com.tdd.pricing.domain.model.Book;

import java.io.Serializable;
import java.util.Map;
// Request DTO
public record BasketRequest(Map<Book,Integer> items) {
}
