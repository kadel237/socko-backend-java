package com.soko_backend.service.report;

import com.soko_backend.dto.report.SalesSummaryResponse;
import com.soko_backend.dto.report.TopProductSalesResponse;
import com.soko_backend.dto.report.SalesTrendPoint;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    SalesSummaryResponse getSalesSummary(LocalDate start, LocalDate end);
    List<TopProductSalesResponse> getTopSellingProducts(int limit, LocalDate start, LocalDate end);
    List<SalesTrendPoint> getSalesTrend(String period, LocalDate start, LocalDate end);
}