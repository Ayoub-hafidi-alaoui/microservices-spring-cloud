package com.ayoub.paymentservice.service;

import com.ayoub.paymentservice.entity.TransactionDetails;
import com.ayoub.paymentservice.model.PaymentMode;
import com.ayoub.paymentservice.model.PaymentRequest;
import com.ayoub.paymentservice.model.PaymentResponse;
import com.ayoub.paymentservice.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Log4j2
@Service
public class PaymentServiceImpl implements PaymentService {
    private TransactionDetailsRepository transactionDetailsRepository;

    public PaymentServiceImpl(TransactionDetailsRepository transactionDetailsRepository) {
        this.transactionDetailsRepository = transactionDetailsRepository;
    }

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("recording payment details {}", paymentRequest);
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();
        transactionDetailsRepository.save(transactionDetails);
        log.info("transaction completed with id: {}", transactionDetails.getId());
        return transactionDetails.getId();

    }

    @Override
    public PaymentResponse getPaymentDetails(long orderId) {
        TransactionDetails transactionDetails = transactionDetailsRepository.findByOrderId(orderId);
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .paymentDate(transactionDetails.getPaymentDate())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .orderId(transactionDetails.getOrderId())
                .amount(transactionDetails.getAmount())
                .build();
        return paymentResponse;
    }
}
