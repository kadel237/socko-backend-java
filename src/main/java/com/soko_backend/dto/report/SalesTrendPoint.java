package com.soko_backend.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesTrendPoint {
    private String period;
    private int orderCount;
    private double revenue;
}