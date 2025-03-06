package com.billcalculationservice.Bill.Calculation.service.service;

import com.billcalculationservice.Bill.Calculation.service.dto.response.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private final String apiUrl = "https://api.exchangerate.host/latest";
    private final String apiKey = "test-api-key";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(exchangeRateService, "apiUrl", apiUrl);
        ReflectionTestUtils.setField(exchangeRateService, "apiKey", apiKey);
    }

    @Test
    void getExchangeRate_WithSameCurrency_ReturnsOne() {
        BigDecimal result = exchangeRateService.getExchangeRate("USD", "USD");

        assertEquals(BigDecimal.ONE, result);

        verifyNoInteractions(restTemplate);
    }

    @Test
    void getExchangeRate_WithNullBaseCurrency_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            exchangeRateService.getExchangeRate(null, "EUR");
        });

        assertEquals("Currency codes cannot be null", exception.getMessage());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void getExchangeRate_WithNullTargetCurrency_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            exchangeRateService.getExchangeRate("USD", null);
        });

        assertEquals("Currency codes cannot be null", exception.getMessage());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void getExchangeRate_WithValidCurrencies_ReturnsCorrectRate() {
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        String url = String.format("%s/%s?apikey=%s", apiUrl, baseCurrency, apiKey);

        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBase(baseCurrency);
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(targetCurrency, new BigDecimal("0.8500"));
        response.setRates(rates);

        ResponseEntity<ExchangeRateResponse> responseEntity = ResponseEntity.ok(response);

        when(restTemplate.getForEntity(url, ExchangeRateResponse.class))
                .thenReturn(responseEntity);

        BigDecimal result = exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);

        assertEquals(new BigDecimal("0.8500").setScale(4, RoundingMode.HALF_UP), result);
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateResponse.class);
    }

    @Test
    void getExchangeRate_WithMissingRate_ThrowsException() {
        String baseCurrency = "USD";
        String targetCurrency = "XYZ";
        String url = String.format("%s/%s?apikey=%s", apiUrl, baseCurrency, apiKey);

        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBase(baseCurrency);
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.8500"));
        response.setRates(rates);

        ResponseEntity<ExchangeRateResponse> responseEntity = ResponseEntity.ok(response);

        when(restTemplate.getForEntity(url, ExchangeRateResponse.class))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);
        });

        assertEquals("Error connecting to exchange rate API", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateResponse.class);
    }

    @Test
    void getExchangeRate_WithNullResponse_ThrowsException() {
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        String url = String.format("%s/%s?apikey=%s", apiUrl, baseCurrency, apiKey);

        ResponseEntity<ExchangeRateResponse> responseEntity = ResponseEntity.ok(null);

        when(restTemplate.getForEntity(url, ExchangeRateResponse.class))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);
        });

        assertEquals("Error connecting to exchange rate API", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateResponse.class);
    }

    @Test
    void getExchangeRate_WithErrorResponse_ThrowsException() {
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        String url = String.format("%s/%s?apikey=%s", apiUrl, baseCurrency, apiKey);

        ResponseEntity<ExchangeRateResponse> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        when(restTemplate.getForEntity(url, ExchangeRateResponse.class))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);
        });

        assertEquals("Error connecting to exchange rate API", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateResponse.class);
    }

    @Test
    void getExchangeRate_WithHttpClientError_ThrowsException() {
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        String url = String.format("%s/%s?apikey=%s", apiUrl, baseCurrency, apiKey);

        HttpClientErrorException clientError = HttpClientErrorException.create(
                HttpStatus.UNAUTHORIZED, "Unauthorized", null, null, null);

        when(restTemplate.getForEntity(url, ExchangeRateResponse.class))
                .thenThrow(clientError);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);
        });

        assertTrue(exception.getMessage().contains("API error"));
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateResponse.class);
    }

    @Test
    void getExchangeRate_WithGenericException_ThrowsException() {
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        String url = String.format("%s/%s?apikey=%s", apiUrl, baseCurrency, apiKey);

        RuntimeException apiException = new RuntimeException("Connection refused");

        when(restTemplate.getForEntity(url, ExchangeRateResponse.class))
                .thenThrow(apiException);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);
        });

        assertEquals("Error connecting to exchange rate API", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateResponse.class);
    }

    @Test
    void convertCurrency_WithValidInputs_ReturnsCorrectResult() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal exchangeRate = new BigDecimal("0.8500");
        BigDecimal expected = new BigDecimal("85.00");

        BigDecimal result = exchangeRateService.convertCurrency(amount, exchangeRate);

        assertEquals(expected, result);
    }

    @Test
    void convertCurrency_WithZeroAmount_ReturnsZero() {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal exchangeRate = new BigDecimal("0.8500");
        BigDecimal expected = new BigDecimal("0.00");

        BigDecimal result = exchangeRateService.convertCurrency(amount, exchangeRate);

        assertEquals(expected, result);
    }

    @Test
    void convertCurrency_WithRoundingNeeded_RoundsCorrectly() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal exchangeRate = new BigDecimal("0.8567");
        BigDecimal expected = new BigDecimal("85.67"); // Rounded to 2 decimal places

        BigDecimal result = exchangeRateService.convertCurrency(amount, exchangeRate);

        assertEquals(expected, result);
        assertEquals(2, result.scale()); // Verify scale is set to 2
    }
}