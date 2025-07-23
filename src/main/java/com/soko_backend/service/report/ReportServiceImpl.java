// ReportServiceImpl.java
package com.soko_backend.service.report;

import com.soko_backend.dto.report.SalesSummaryResponse;
import com.soko_backend.dto.report.TopProductSalesResponse;
import com.soko_backend.dto.report.SalesTrendPoint;
import com.soko_backend.entity.order.Order;
import com.soko_backend.entity.order.OrderItem;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.enums.OrderStatus;
import com.soko_backend.repository.OrderRepository;
import com.soko_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderRepository orderRepository;
    private final CurrentUserService currentUserService;

    @Override
    public SalesSummaryResponse getSalesSummary(LocalDate start, LocalDate end) {
        UserEntity merchant = currentUserService.getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non connecté"));

        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().toLocalDate().compareTo(start) >= 0)
                .filter(order -> order.getCreatedAt().toLocalDate().compareTo(end) <= 0)
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item.getProduct().getShop().getOwner().getId().equals(merchant.getId())))
                .toList();

        double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        int totalOrders = orders.size();
        double average = totalOrders > 0 ? totalRevenue / totalOrders : 0;

        return new SalesSummaryResponse(totalOrders, totalRevenue, average, start, end);
    }

    @Override
    public List<TopProductSalesResponse> getTopSellingProducts(int limit, LocalDate start, LocalDate end) {
        UserEntity merchant = currentUserService.getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non connecté"));

        List<OrderItem> items = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().toLocalDate().compareTo(start) >= 0)
                .filter(order -> order.getCreatedAt().toLocalDate().compareTo(end) <= 0)
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .flatMap(order -> order.getItems().stream())
                .filter(item -> item.getProduct().getShop().getOwner().getId().equals(merchant.getId()))
                .toList();

        Map<String, TopProductSalesResponse> stats = new HashMap<>();

        for (OrderItem item : items) {
            String name = item.getProduct().getName();
            stats.putIfAbsent(name, new TopProductSalesResponse(name, 0, 0.0));

            TopProductSalesResponse existing = stats.get(name);
            existing.setQuantitySold(existing.getQuantitySold() + item.getQuantity());
            existing.setRevenue(existing.getRevenue() + item.getTotalPrice());
        }

        return stats.values().stream()
                .sorted(Comparator.comparingLong(TopProductSalesResponse::getQuantitySold).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    @Override
    public List<SalesTrendPoint> getSalesTrend(String period, LocalDate start, LocalDate end) {
        UserEntity merchant = currentUserService.getCurrentUser()
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non connecté"));

        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().toLocalDate().compareTo(start) >= 0)
                .filter(order -> order.getCreatedAt().toLocalDate().compareTo(end) <= 0)
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item.getProduct().getShop().getOwner().getId().equals(merchant.getId())))
                .toList();

        Map<String, List<Order>> grouped = new HashMap<>();

        for (Order order : orders) {
            LocalDate date = order.getCreatedAt().toLocalDate();
            String key;
            if ("weekly".equalsIgnoreCase(period)) {
                WeekFields wf = WeekFields.of(Locale.getDefault());
                int week = date.get(wf.weekOfWeekBasedYear());
                int year = date.getYear();
                key = year + "-W" + String.format("%02d", week);
            } else if ("monthly".equalsIgnoreCase(period)) {
                key = date.getYear() + "-" + date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
            } else {
                throw new IllegalArgumentException("Période non supportée : " + period);
            }

            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(order);
        }

        return grouped.entrySet().stream()
                .map(entry -> {
                    int count = entry.getValue().size();
                    double revenue = entry.getValue().stream().mapToDouble(Order::getTotalAmount).sum();
                    return new SalesTrendPoint(entry.getKey(), count, revenue);
                })
                .sorted(Comparator.comparing(SalesTrendPoint::getPeriod))
                .collect(Collectors.toList());
    }
}
