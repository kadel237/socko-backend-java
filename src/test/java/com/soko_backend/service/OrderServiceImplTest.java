package com.soko_backend.service;


import com.soko_backend.dto.order.CreateOrderRequest;
import com.soko_backend.dto.order.CreateOrderRequest.OrderItemRequest;

import com.soko_backend.entity.order.Order;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.enums.OrderStatus;
import com.soko_backend.repository.OrderRepository;
import com.soko_backend.repository.ProductRepository;
import com.soko_backend.repository.UserRepository;
import com.soko_backend.service.order.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        userRepository = mock(UserRepository.class);
        orderService = new OrderServiceImpl(orderRepository, productRepository, userRepository);
    }

    @Test
    void createOrder_shouldCreateSuccessfully() {
        // setup
        Long userId = 1L;
        Long productId = 10L;
        int quantity = 2;
        double price = 20.0;

        UserEntity user = UserEntity.builder().id(userId).build();
        Product product = Product.builder()
                .id(productId)
                .name("Test Product")
                .price(price)
                .stockQuantity(10)
                .build();

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(userId);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(productId);
        itemRequest.setQuantity(quantity);
        request.setItems(List.of(itemRequest));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // execute
        var response = orderService.createOrder(request);

        // verify
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertEquals(1, response.getItems().size());
        assertEquals(40.0, response.getTotalAmount());

        // stock updated
        assertEquals(8, product.getStockQuantity());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrdersByCustomer_shouldReturnList() {
        UserEntity user = UserEntity.builder().id(1L).build();
        Order order = Order.builder().id(1L).customer(user).items(Collections.emptyList()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findByCustomer(user)).thenReturn(List.of(order));

        var orders = orderService.getOrdersByCustomer(1L);

        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());
    }

    @Test
    void getOrderById_shouldReturnOrder() {
        Order order = Order.builder().id(1L).customer(UserEntity.builder().id(1L).build()).items(Collections.emptyList()).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        var response = orderService.getOrderById(1L);

        assertEquals(1L, response.getId());
    }
}