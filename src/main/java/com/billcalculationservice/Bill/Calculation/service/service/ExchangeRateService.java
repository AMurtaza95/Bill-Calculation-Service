package com.billcalculationservice.Bill.Calculation.service.service;


import com.billcalculationservice.Bill.Calculation.service.dto.response.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {

    private static final BigDecimal ONE = BigDecimal.ONE;
    private static final int EXCHANGE_RATE_SCALE = 4;
    private static final int AMOUNT_SCALE = 2;

    private final RestTemplate restTemplate;

    @Value("${exchange.api.url}")
    private String apiUrl;

    @Value("${exchange.api.key}")
    private String apiKey;

    @Cacheable(value = "exchangeRates", key = "#baseCurrency + '_' + #targetCurrency")
    public BigDecimal getExchangeRate(String baseCurrency, String targetCurrency) {
        if (baseCurrency == null || targetCurrency == null) {
            throw new IllegalArgumentException("Currency codes cannot be null");
        }

        if (baseCurrency.equals(targetCurrency)) {
            return ONE;
        }

        log.debug("Fetching exchange rate from {} to {}", baseCurrency, targetCurrency);
        String url = String.format("%s/%s?apikey=%s", apiUrl, baseCurrency, apiKey);

        try {
            ResponseEntity<ExchangeRateResponse> responseEntity =
                    restTemplate.getForEntity(url, ExchangeRateResponse.class);

            if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                throw new RuntimeException("Failed to retrieve exchange rates, status: " + responseEntity.getStatusCode());
            }

            ExchangeRateResponse response = responseEntity.getBody();
            BigDecimal rate = response.getRates().get(targetCurrency);

            if (rate == null) {
                throw new RuntimeException("Exchange rate not found for currency: " + targetCurrency);
            }

            return rate.setScale(EXCHANGE_RATE_SCALE, RoundingMode.HALF_UP);

        } catch (HttpClientErrorException e) {
            log.error("API error: status={}, message={}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("API error: " + e.getStatusCode() + " " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error fetching exchange rate", e);
            throw new RuntimeException("Error connecting to exchange rate API", e);
        }
    }

    public BigDecimal convertCurrency(BigDecimal amount, BigDecimal exchangeRate) {
        return amount.multiply(exchangeRate).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
    }
}