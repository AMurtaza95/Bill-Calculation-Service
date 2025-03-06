package com.billcalculationservice.Bill.Calculation.service.service;

import com.billcalculationservice.Bill.Calculation.service.dto.request.Bill;
import com.billcalculationservice.Bill.Calculation.service.dto.request.Item;
import com.billcalculationservice.Bill.Calculation.service.dto.request.User;
import com.billcalculationservice.Bill.Calculation.service.dto.response.BillResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.billcalculationservice.Bill.Calculation.service.Fixtures.bill;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {

    @Mock
    private DiscountService discountService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private BillService billService;

    private Bill sampleBill;

    @BeforeEach
    void setUp() {
        sampleBill = bill;
    }

    @Test
    void calculatePayableAmount_ShouldReturnCorrectResponse() {
        BigDecimal discountAmount = new BigDecimal("100.00");
        BigDecimal amountAfterDiscount = new BigDecimal("902.00"); // 1002 - 100
        BigDecimal exchangeRate = new BigDecimal("0.85");
        BigDecimal payableAmount = new BigDecimal("766.70"); // 902 * 0.85

        when(discountService.calculateDiscount(any(Bill.class))).thenReturn(discountAmount);
        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(exchangeRate);
        when(exchangeRateService.convertCurrency(amountAfterDiscount, exchangeRate)).thenReturn(payableAmount);

        BillResponse response = billService.calculatePayableAmount(sampleBill);

        assertNotNull(response);
        assertEquals(payableAmount, response.getPayableAmount());
        assertEquals("EUR", response.getTargetCurrency());

        verify(discountService, times(1)).calculateDiscount(sampleBill);
        verify(exchangeRateService, times(1)).getExchangeRate("USD", "EUR");
        verify(exchangeRateService, times(1)).convertCurrency(amountAfterDiscount, exchangeRate);
    }

    @Test
    void calculatePayableAmount_WithZeroDiscount_ShouldReturnCorrectResponse() {
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal amountAfterDiscount = new BigDecimal("1002.00"); // 1002 - 0
        BigDecimal exchangeRate = new BigDecimal("0.85");
        BigDecimal payableAmount = new BigDecimal("851.70"); // 1002 * 0.85

        when(discountService.calculateDiscount(any(Bill.class))).thenReturn(discountAmount);
        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(exchangeRate);
        when(exchangeRateService.convertCurrency(amountAfterDiscount, exchangeRate)).thenReturn(payableAmount);

        BillResponse response = billService.calculatePayableAmount(sampleBill);

        assertNotNull(response);
        assertEquals(payableAmount, response.getPayableAmount());
        assertEquals("EUR", response.getTargetCurrency());
    }

    @Test
    void calculatePayableAmount_WithSameCurrency_ShouldReturnCorrectResponse() {
        Bill billWithSameCurrency = Bill.builder()
                .items(sampleBill.getItems())
                .totalAmount(new BigDecimal("1002.00"))
                .originalCurrency("USD")
                .targetCurrency("USD") // Same currency
                .user(sampleBill.getUser())
                .build();

        BigDecimal discountAmount = new BigDecimal("100.00");
        BigDecimal amountAfterDiscount = new BigDecimal("902.00"); // 1002 - 100
        BigDecimal exchangeRate = BigDecimal.ONE; // Same currency rate
        BigDecimal payableAmount = new BigDecimal("902.00"); // Same as amountAfterDiscount

        when(discountService.calculateDiscount(any(Bill.class))).thenReturn(discountAmount);
        when(exchangeRateService.getExchangeRate("USD", "USD")).thenReturn(exchangeRate);
        when(exchangeRateService.convertCurrency(amountAfterDiscount, exchangeRate)).thenReturn(payableAmount);

        BillResponse response = billService.calculatePayableAmount(billWithSameCurrency);

        assertNotNull(response);
        assertEquals(payableAmount, response.getPayableAmount());
        assertEquals("USD", response.getTargetCurrency());
    }

    @Test
    void calculatePayableAmount_WithFullDiscount_ShouldReturnZeroPayable() {
        BigDecimal discountAmount = new BigDecimal("1002.00");
        BigDecimal exchangeRate = new BigDecimal("0.85");
        BigDecimal payableAmount = BigDecimal.ZERO; // 0 * 0.85 = 0

        when(discountService.calculateDiscount(any(Bill.class))).thenReturn(discountAmount);
        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(exchangeRate);
        when(exchangeRateService.convertCurrency(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(payableAmount);

        BillResponse response = billService.calculatePayableAmount(sampleBill);

        assertNotNull(response);
        assertEquals(payableAmount, response.getPayableAmount());
        assertEquals("EUR", response.getTargetCurrency());

        verify(exchangeRateService).convertCurrency(
                argThat(bd -> bd.compareTo(BigDecimal.ZERO) == 0),
                argThat(bd -> bd.compareTo(new BigDecimal("0.85")) == 0)
        );
    }

    @Test
    void calculatePayableAmount_ShouldHandleExceptionalCases() {
        when(discountService.calculateDiscount(any(Bill.class))).thenThrow(new RuntimeException("Test exception"));

        try {
            billService.calculatePayableAmount(sampleBill);
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }

        verify(discountService, times(1)).calculateDiscount(sampleBill);
        verify(exchangeRateService, never()).getExchangeRate(anyString(), anyString());
        verify(exchangeRateService, never()).convertCurrency(any(BigDecimal.class), any(BigDecimal.class));
    }
}