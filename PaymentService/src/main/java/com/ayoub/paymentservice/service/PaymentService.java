package com.ayoub.paymentservice.service;

import com.ayoub.paymentservice.model.PaymentRequest;
import com.ayoub.paymentservice.model.PaymentResponse;

public interface PaymentService {
    public long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetails(long orderId);
}
