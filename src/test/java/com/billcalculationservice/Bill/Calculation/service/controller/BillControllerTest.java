package com.billcalculationservice.Bill.Calculation.service.controller;

import com.billcalculationservice.Bill.Calculation.service.dto.request.Bill;
import com.billcalculationservice.Bill.Calculation.service.dto.response.BillResponse;
import com.billcalculationservice.Bill.Calculation.service.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

import static com.billcalculationservice.Bill.Calculation.service.Fixtures.createSampleBill;
import static com.billcalculationservice.Bill.Calculation.service.Fixtures.createSampleResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillControllerTest {

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    private Bill sampleBill;
    private BillResponse sampleResponse;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user", "password"));

        sampleBill = createSampleBill();
        sampleResponse = createSampleResponse();
    }

    @Test
    public void testConstructor() {
        BillController controller = new BillController(billService);

        assertNotNull(controller);
    }

    @Test
    public void testCalculatePayableAmount_Success() {
        when(billService.calculatePayableAmount(any(Bill.class))).thenReturn(sampleResponse);

        ResponseEntity<BillResponse> response = billController.calculatePayableAmount(sampleBill);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sampleResponse, response.getBody());
        assertEquals(new BigDecimal("95.50"), response.getBody().getPayableAmount());
        assertEquals("EUR", response.getBody().getTargetCurrency());

        verify(billService, times(1)).calculatePayableAmount(sampleBill);
    }

    @Test
    public void testCalculatePayableAmount_WithNullPayableAmount() {
        BillResponse responseWithNullAmount = BillResponse.builder()
                .payableAmount(null)
                .targetCurrency("EUR")
                .build();

        when(billService.calculatePayableAmount(any(Bill.class))).thenReturn(responseWithNullAmount);

        ResponseEntity<BillResponse> response = billController.calculatePayableAmount(sampleBill);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getPayableAmount());

        verify(billService, times(1)).calculatePayableAmount(sampleBill);
    }

    @Test
    public void testCalculatePayableAmount_WithServiceException() {
        when(billService.calculatePayableAmount(any(Bill.class)))
                .thenThrow(new RuntimeException("Test exception"));

        assertThrows(RuntimeException.class, () -> {
            billController.calculatePayableAmount(sampleBill);
        });

        verify(billService, times(1)).calculatePayableAmount(sampleBill);
    }

    @Test
    public void testCalculatePayableAmount_WithZeroAmount() {
        BillResponse zeroResponse = BillResponse.builder()
                .payableAmount(BigDecimal.ZERO)
                .targetCurrency("EUR")
                .build();

        when(billService.calculatePayableAmount(any(Bill.class))).thenReturn(zeroResponse);

        ResponseEntity<BillResponse> response = billController.calculatePayableAmount(sampleBill);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.ZERO, response.getBody().getPayableAmount());

        verify(billService, times(1)).calculatePayableAmount(sampleBill);
    }
}