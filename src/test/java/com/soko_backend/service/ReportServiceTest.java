package com.soko_backend.service;

import com.soko_backend.dto.report.SalesSummaryResponse;
import com.soko_backend.dto.report.TopProductSalesResponse;
import com.soko_backend.entity.order.Order;
import com.soko_backend.entity.order.OrderItem;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.shop.ShopEntity;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.enums.OrderStatus;
import com.soko_backend.repository.OrderRepository;
import com.soko_backend.security.CurrentUserService;
import com.soko_backend.service.report.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CurrentUserService currentUserService;
    @InjectMocks
    private ReportServiceImpl reportService;

    private UserEntity merchant;
    private Product product;
    private ShopEntity shop;
    private Order order;
    private OrderItem item;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        merchant = new UserEntity();
        merchant.setId(1L);

        shop = new ShopEntity();
        shop.setOwner(merchant);

        product = new Product();
        product.setName("Produit Test");
        product.setShop(shop);
        product.setPrice(100.0);

        item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(100.0);


        order = new Order();
        order.setStatus(OrderStatus.VALIDATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(List.of(item));
        order.setTotalAmount(200.0);
    }

    @Test
    void testGetSalesSummary() {
        when(currentUserService.getCurrentUser()).thenReturn(Optional.of(merchant));
        when(orderRepository.findAll()).thenReturn(List.of(order));

        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);

        SalesSummaryResponse summary = reportService.getSalesSummary(start, end);

        assertEquals(1, summary.getTotalOrders());
        assertEquals(200.0, summary.getTotalRevenue());
        assertEquals(200.0, summary.getAverageOrderValue());
    }

    @Test
    void testGetTopSellingProducts() {
        when(currentUserService.getCurrentUser()).thenReturn(Optional.of(merchant));
        when(orderRepository.findAll()).thenReturn(List.of(order));

        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);

        List<TopProductSalesResponse> topProducts = reportService.getTopSellingProducts(3, start, end);

        assertEquals(1, topProducts.size());
        assertEquals("Produit Test", topProducts.get(0).getProductName());
        assertEquals(2, topProducts.get(0).getQuantitySold());
        assertEquals(200.0, topProducts.get(0).getRevenue());
    }
}
