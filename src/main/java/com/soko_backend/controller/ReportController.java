// ReportController.java
package com.soko_backend.controller;

import com.soko_backend.dto.report.SalesSummaryResponse;
import com.soko_backend.dto.report.TopProductSalesResponse;
import com.soko_backend.dto.report.SalesTrendPoint;
import com.soko_backend.service.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports/sales")
@RequiredArgsConstructor
@Tag(name = "Rapports de Vente", description = "Statistiques commerciales pour les commerçants")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Résumé des ventes sur une période")
    @GetMapping("/summary")
    public ResponseEntity<SalesSummaryResponse> getSalesSummary(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(reportService.getSalesSummary(start, end));
    }

    @Operation(summary = "Top produits vendus sur une période")
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductSalesResponse>> getTopProducts(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(reportService.getTopSellingProducts(limit, start, end));
    }

    @Operation(summary = "Évolution des ventes (weekly/monthly)")
    @GetMapping("/trend")
    public ResponseEntity<List<SalesTrendPoint>> getSalesTrend(
            @RequestParam String period,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(reportService.getSalesTrend(period, start, end));
    }
}
