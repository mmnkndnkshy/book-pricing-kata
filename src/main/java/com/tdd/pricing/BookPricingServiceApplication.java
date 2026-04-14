package com.tdd.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookPricingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookPricingServiceApplication.class, args);
	}

}
