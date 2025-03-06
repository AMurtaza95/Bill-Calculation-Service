package com.billcalculationservice.Bill.Calculation.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private BigDecimal payableAmount;
    private String targetCurrency;
}