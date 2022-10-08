package com.ayoub.orderservice.service;

import com.ayoub.orderservice.entity.Order;
import com.ayoub.orderservice.entity.OrderStatus;
import com.ayoub.orderservice.exception.CustomException;
import com.ayoub.orderservice.external.client.PaymentService;
import com.ayoub.orderservice.external.client.ProductService;
import com.ayoub.orderservice.external.requests.PaymentRequest;
import com.ayoub.orderservice.external.response.PaymentResponse;
import com.ayoub.orderservice.model.OrderRequest;
import com.ayoub.orderservice.model.OrderResponse;
import com.ayoub.orderservice.repository.OrderRepository;
import com.ayoub.productservice.model.ProductResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static com.ayoub.orderservice.entity.OrderStatus.CREATED;

@Service
@Log4j2
public class OrderServiceImpl implements IOrderService {

    private OrderRepository orderRepository;
    private PaymentService paymentService;
    private RestTemplate restTemplate;
    private ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentService paymentService, RestTemplate restTemplate, ProductService productService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
        this.productService = productService;
    }

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("placing order...");
        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .build();
        log.info("calling payment service to complete the payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        log.info("order was placed succefully with id: {}", order.getId());
        paymentService.doPayment(paymentRequest);
        try {
            log.info("Payment done successfully, changing the order status");
            order.setOrderStatus(CREATED);
        } catch (Exception e) {
            log.error("Error occur in payment ");
            //order.setOrderStatus(OrderStatus.FAILED);
        }
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderid) {
        Order order = orderRepository.findById(orderid).orElseThrow(() -> new CustomException("order does not exist", "NOT FOUND", 404));
        log.info("invoking product service to fetch the product whose id: {}", order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class);
        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();
        log.info("invoking payment service to fetch the payment info");
        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + orderid, PaymentResponse.class);
        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .status(paymentResponse.getStatus())
                .build();
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;


    }
}
