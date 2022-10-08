package com.ayoub.orderservice.service;

import com.ayoub.orderservice.model.OrderRequest;
import com.ayoub.orderservice.model.OrderResponse;

public interface IOrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderid);
}
