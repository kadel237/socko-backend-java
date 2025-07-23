package com.soko_backend.service.order;

import com.soko_backend.dto.order.CreateOrderRequest;
import com.soko_backend.dto.order.OrderItemResponse;
import com.soko_backend.dto.order.OrderResponse;
import com.soko_backend.entity.order.Order;
import com.soko_backend.entity.order.OrderItem;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.enums.OrderStatus;
import com.soko_backend.repository.OrderRepository;
import com.soko_backend.repository.ProductRepository;
import com.soko_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        UserEntity customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING)
                .build();

        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Stock insuffisant pour : " + product.getName());
            }

            double unitPrice = product.getPrice();
            int quantity = itemRequest.getQuantity();

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .build();

            total += unitPrice * quantity;
            orderItems.add(item);

            // Optionnel : décrémenter le stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        return toDto(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        return orderRepository.findByCustomer(customer)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));
        return toDto(order);
    }

    private OrderResponse toDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream()
                        .map(item -> OrderItemResponse.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}