package com.soko_backend.service.order;

import com.soko_backend.dto.order.CreateOrderRequest;
import com.soko_backend.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    List<OrderResponse> getOrdersByCustomer(Long customerId);
    OrderResponse getOrderById(Long orderId);
}