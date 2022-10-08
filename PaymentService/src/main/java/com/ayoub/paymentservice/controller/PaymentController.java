package com.ayoub.paymentservice.controller;

import com.ayoub.paymentservice.model.PaymentRequest;
import com.ayoub.paymentservice.model.PaymentResponse;
import com.ayoub.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.doPayment(paymentRequest), HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(@PathVariable long orderId) {
        return new ResponseEntity<>(paymentService.getPaymentDetails(orderId), HttpStatus.OK);
    }
}
