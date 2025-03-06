package com.billcalculationservice.Bill.Calculation.service.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BillResponseTest {

    @Test
    public void testNoArgsConstructor() {
        BillResponse response = new BillResponse();

        assertNull(response.getPayableAmount());
        assertNull(response.getTargetCurrency());
    }

    @Test
    public void testAllArgsConstructor() {
        BigDecimal amount = new BigDecimal("100.50");
        String currency = "USD";
        BillResponse response = new BillResponse(amount, currency);

        assertEquals(amount, response.getPayableAmount());
        assertEquals(currency, response.getTargetCurrency());
    }

    @Test
    public void testBuilder() {
        BigDecimal amount = new BigDecimal("100.50");
        String currency = "USD";
        BillResponse response = BillResponse.builder()
                .payableAmount(amount)
                .targetCurrency(currency)
                .build();

        assertEquals(amount, response.getPayableAmount());
        assertEquals(currency, response.getTargetCurrency());
    }

    @Test
    public void testBuilderToString() {
        BillResponse.BillResponseBuilder builder = BillResponse.builder()
                .payableAmount(new BigDecimal("100.50"))
                .targetCurrency("USD");

        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("BillResponseBuilder"));
        assertTrue(builderString.contains("payableAmount=100.50"));
        assertTrue(builderString.contains("targetCurrency=USD"));
    }

    @Test
    public void testGettersAndSetters() {
        BillResponse response = new BillResponse();

        BigDecimal amount = new BigDecimal("100.50");
        String currency = "USD";
        response.setPayableAmount(amount);
        response.setTargetCurrency(currency);

        assertEquals(amount, response.getPayableAmount());
        assertEquals(currency, response.getTargetCurrency());
    }

    @Test
    public void testEqualsAndHashCode() {
        BigDecimal amount = new BigDecimal("100.50");
        String currency = "USD";
        BillResponse response1 = new BillResponse(amount, currency);
        BillResponse response2 = new BillResponse(amount, currency);

        assertEquals(response1, response2);
        assertEquals(response1, response1);

        assertEquals(response1.hashCode(), response2.hashCode());

        BillResponse differentAmount = new BillResponse(new BigDecimal("200.00"), currency);
        BillResponse differentCurrency = new BillResponse(amount, "EUR");

        assertNotEquals(response1, differentAmount);
        assertNotEquals(response1, differentCurrency);
        assertNotEquals(null, response1);
        assertNotEquals(new Object(), response1);
    }

    @Test
    public void testToString() {
        BigDecimal amount = new BigDecimal("100.50");
        String currency = "USD";
        BillResponse response = new BillResponse(amount, currency);

        String toString = response.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("payableAmount=" + amount));
        assertTrue(toString.contains("targetCurrency=" + currency));
    }

    @Test
    public void testWithNullValues() {
        BillResponse response = new BillResponse(null, null);

        assertNull(response.getPayableAmount());
        assertNull(response.getTargetCurrency());

        BillResponse alsoNull = new BillResponse(null, null);
        assertEquals(response, alsoNull);
        assertEquals(response.hashCode(), alsoNull.hashCode());
    }
}