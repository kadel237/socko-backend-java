package com.soko_backend.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesSummaryResponse {
    private int totalOrders;
    private double totalRevenue;
    private double averageOrderValue;
    private LocalDate startDate;
    private LocalDate endDate;
}