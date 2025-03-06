package com.billcalculationservice.Bill.Calculation.service.service;


import com.billcalculationservice.Bill.Calculation.service.dto.request.Bill;
import com.billcalculationservice.Bill.Calculation.service.dto.response.BillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BillService {

    private final DiscountService discountService;
    private final ExchangeRateService exchangeRateService;

    public BillResponse calculatePayableAmount(Bill requestDTO) {

        BigDecimal discountAmount = discountService.calculateDiscount(requestDTO);

        BigDecimal amountAfterDiscount = requestDTO.getTotalAmount().subtract(discountAmount);

        BigDecimal exchangeRate = exchangeRateService.getExchangeRate(
                requestDTO.getOriginalCurrency(),
                requestDTO.getTargetCurrency());

        BigDecimal payableAmount = exchangeRateService.convertCurrency(amountAfterDiscount, exchangeRate);

        return BillResponse.builder()
                .payableAmount(payableAmount)
                .targetCurrency(requestDTO.getTargetCurrency())
                .build();
    }
}