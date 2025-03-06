package com.billcalculationservice.Bill.Calculation.service;

import com.billcalculationservice.Bill.Calculation.service.dto.request.Bill;
import com.billcalculationservice.Bill.Calculation.service.dto.request.Item;
import com.billcalculationservice.Bill.Calculation.service.dto.request.User;
import com.billcalculationservice.Bill.Calculation.service.dto.response.BillResponse;

import java.math.BigDecimal;
import java.util.List;

public class Fixtures {
    // Helper methods to create test data
    public static Bill createSampleBill() {
        Item item1 = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("1200.00"))
                .build();

        Item item2 = Item.builder()
                .name("Apple")
                .category("GROCERY")
                .price(new BigDecimal("3.99"))
                .build();

        User user = User.builder()
                .type("CUSTOMER")
                .tenureInYears(3)
                .build();

        return Bill.builder()
                .items(List.of(item1, item2))
                .totalAmount(new BigDecimal("1203.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(user)
                .build();
    }

    public static BillResponse createSampleResponse() {
        return BillResponse.builder()
                .payableAmount(new BigDecimal("95.50"))
                .targetCurrency("EUR")
                .build();
    }

    public static Item item1 = Item.builder()
            .name("Laptop")
            .category("ELECTRONICS")
            .price(new BigDecimal("1000.00"))
            .build();

    public static Item item2 = Item.builder()
            .name("Apple")
            .category("GROCERY")
            .price(new BigDecimal("2.00"))
            .build();

    static User user = User.builder()
            .type("CUSTOMER")
            .tenureInYears(3)
            .build();

    public static Bill bill = Bill.builder()
            .items(List.of(item1, item2))
            .totalAmount(new BigDecimal("1002.00"))
            .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(user)
                .build();
}
