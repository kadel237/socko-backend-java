package com.soko_backend.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProductSalesResponse {
    private String productName;
    private long quantitySold;
    private double revenue;
}