package com.ayoub.orderservice.external.client;

import com.ayoub.orderservice.exception.CustomException;
import com.ayoub.orderservice.external.requests.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient("PAYMENT-SERVICE/payment")
public interface PaymentService {
    @RequestMapping
    @PostMapping
    ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void fallback(Exception e) {
        throw new CustomException("Payment services not available", "UNAVAILABLE", 500);
    }
}
