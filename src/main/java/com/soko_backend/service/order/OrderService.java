package com.soko_backend.service.order;

import com.soko_backend.dto.order.CreateOrderRequest;
import com.soko_backend.dto.order.OrderResponse;
import com.soko_backend.dto.order.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    List<OrderResponse> getOrdersByCustomer(Long customerId);

    List<OrderResponse> getOrdersForCurrentCustomer();

    List<OrderResponse> getOrdersForCurrentMerchant();

    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

    OrderResponse getOrderById(Long orderId);
}