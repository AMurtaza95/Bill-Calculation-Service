package com.billcalculationservice.Bill.Calculation.service.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateResponseTest {

    @Test
    public void testNoArgsConstructor() {
        ExchangeRateResponse response = new ExchangeRateResponse();

        assertNull(response.getDisclaimer());
        assertNull(response.getLicense());
        assertEquals(0L, response.getTimestamp());
        assertNull(response.getBase());
        assertNull(response.getRates());
    }

    @Test
    public void testGettersAndSetters() {
        ExchangeRateResponse response = new ExchangeRateResponse();

        String disclaimer = "Sample disclaimer";
        String license = "Sample license";
        long timestamp = 1234567890L;
        String base = "USD";
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        rates.put("GBP", new BigDecimal("0.75"));

        response.setDisclaimer(disclaimer);
        response.setLicense(license);
        response.setTimestamp(timestamp);
        response.setBase(base);
        response.setRates(rates);

        assertEquals(disclaimer, response.getDisclaimer());
        assertEquals(license, response.getLicense());
        assertEquals(timestamp, response.getTimestamp());
        assertEquals(base, response.getBase());
        assertEquals(rates, response.getRates());
    }

    @Test
    public void testEqualsAndHashCode() {
        ExchangeRateResponse response1 = new ExchangeRateResponse();
        ExchangeRateResponse response2 = new ExchangeRateResponse();

        String disclaimer = "Sample disclaimer";
        String license = "Sample license";
        long timestamp = 1234567890L;
        String base = "USD";
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));

        response1.setDisclaimer(disclaimer);
        response1.setLicense(license);
        response1.setTimestamp(timestamp);
        response1.setBase(base);
        response1.setRates(rates);

        response2.setDisclaimer(disclaimer);
        response2.setLicense(license);
        response2.setTimestamp(timestamp);
        response2.setBase(base);
        response2.setRates(new HashMap<>(rates));

        assertEquals(response1, response2);
        assertEquals(response1, response1);

        assertEquals(response1.hashCode(), response2.hashCode());

        ExchangeRateResponse different = new ExchangeRateResponse();
        different.setDisclaimer(disclaimer);
        different.setLicense(license);
        different.setTimestamp(timestamp + 1);
        different.setBase(base);
        different.setRates(rates);

        assertNotEquals(response1, different);
        assertNotEquals(null, response1);
        assertNotEquals(new Object(), response1);
    }

    @Test
    public void testToString() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        String disclaimer = "Sample disclaimer";
        String license = "Sample license";
        long timestamp = 1234567890L;
        String base = "USD";
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));

        response.setDisclaimer(disclaimer);
        response.setLicense(license);
        response.setTimestamp(timestamp);
        response.setBase(base);
        response.setRates(rates);

        String toString = response.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("disclaimer=" + disclaimer));
        assertTrue(toString.contains("license=" + license));
        assertTrue(toString.contains("timestamp=" + timestamp));
        assertTrue(toString.contains("base=" + base));
        assertTrue(toString.contains("rates=" + rates));
    }

    @Test
    public void testJsonIgnorePropertiesAnnotation() {

        assertTrue(ExchangeRateResponse.class.isAnnotationPresent(
                com.fasterxml.jackson.annotation.JsonIgnoreProperties.class));

        com.fasterxml.jackson.annotation.JsonIgnoreProperties annotation =
                ExchangeRateResponse.class.getAnnotation(
                        com.fasterxml.jackson.annotation.JsonIgnoreProperties.class);

        assertTrue(annotation.ignoreUnknown());
    }

    @Test
    public void testWithNullValues() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setDisclaimer(null);
        response.setLicense(null);
        response.setBase(null);
        response.setRates(null);

        assertNull(response.getDisclaimer());
        assertNull(response.getLicense());
        assertNull(response.getBase());
        assertNull(response.getRates());

        ExchangeRateResponse alsoNull = new ExchangeRateResponse();
        alsoNull.setDisclaimer(null);
        alsoNull.setLicense(null);
        alsoNull.setBase(null);
        alsoNull.setRates(null);

        assertEquals(response, alsoNull);
        assertEquals(response.hashCode(), alsoNull.hashCode());
    }

    @Test
    public void testRatesManipulation() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        response.setRates(rates);

        Map<String, BigDecimal> retrievedRates = response.getRates();
        retrievedRates.put("GBP", new BigDecimal("0.75"));

        assertEquals(2, response.getRates().size());
        assertEquals(new BigDecimal("0.85"), response.getRates().get("EUR"));
        assertEquals(new BigDecimal("0.75"), response.getRates().get("GBP"));
    }
}