package com.ayoub.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallBack() {
        return "service order is down";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallBack() {
        return "service product is down";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallBack() {
        return "service payment is down";
    }
}
