package com.ayoub.orderservice.controller;

import com.ayoub.orderservice.external.client.PaymentService;
import com.ayoub.orderservice.external.client.ProductService;
import com.ayoub.orderservice.model.OrderRequest;
import com.ayoub.orderservice.model.OrderResponse;
import com.ayoub.orderservice.service.IOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {
    private IOrderService orderService;


    @Autowired
    private ProductService productService;




    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {
        //productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("update the product with id: {}", orderRequest.getProductId());
        long orderId = orderService.placeOrder(orderRequest);
        log.info("order id: {}", orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @GetMapping("/{orderid}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderid) {
        OrderResponse orderResponse = orderService.getOrderDetails(orderid);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
