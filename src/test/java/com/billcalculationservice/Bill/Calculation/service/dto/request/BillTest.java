package com.billcalculationservice.Bill.Calculation.service.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class BillTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidBill() {
        Item item = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"))
                .build();

        User user = User.builder()
                .type("CUSTOMER")
                .tenureInYears(3)
                .build();

        Bill bill = Bill.builder()
                .items(List.of(item))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(user)
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertTrue(violations.isEmpty(), "Valid bill should not have validation errors");
    }

    @Test
    public void testEmptyItemsList() {
        Bill bill = Bill.builder()
                .items(new ArrayList<>())
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build())
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Items list cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullItemsList() {
        Bill bill = Bill.builder()
                .items(null)
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build())
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Items list cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullTotalAmount() {
        Bill bill = Bill.builder()
                .items(List.of(Item.builder().name("Laptop").category("ELECTRONICS").price(new BigDecimal("999.99")).build()))
                .totalAmount(null)
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build())
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Total amount cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativeTotalAmount() {
        Bill bill = Bill.builder()
                .items(List.of(Item.builder().name("Laptop").category("ELECTRONICS").price(new BigDecimal("999.99")).build()))
                .totalAmount(new BigDecimal("-10.00"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build())
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Total amount must be non-negative", violations.iterator().next().getMessage());
    }

    @Test
    public void testBlankOriginalCurrency() {
        Bill bill = Bill.builder()
                .items(List.of(Item.builder().name("Laptop").category("ELECTRONICS").price(new BigDecimal("999.99")).build()))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("")
                .targetCurrency("EUR")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build())
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Original currency is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testBlankTargetCurrency() {
        Bill bill = Bill.builder()
                .items(List.of(Item.builder().name("Laptop").category("ELECTRONICS").price(new BigDecimal("999.99")).build()))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build())
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Target currency is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullUser() {
        Bill bill = Bill.builder()
                .items(List.of(Item.builder().name("Laptop").category("ELECTRONICS").price(new BigDecimal("999.99")).build()))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(null)
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("User information is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidItem() {
        Item invalidItem = Item.builder()
                .name("")
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"))
                .build();

        Bill bill = Bill.builder()
                .items(List.of(invalidItem))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build())
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertFalse(violations.isEmpty(), "Bill with invalid item should have validation errors");
    }

    @Test
    public void testInvalidUser() {
        User invalidUser = User.builder()
                .type("")
                .tenureInYears(3)
                .build();

        Bill bill = Bill.builder()
                .items(List.of(Item.builder().name("Laptop").category("ELECTRONICS").price(new BigDecimal("999.99")).build()))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(invalidUser)
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertFalse(violations.isEmpty(), "Bill with invalid user should have validation errors");
    }

    @Test
    public void testLombokMethods() {
        Item item = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"))
                .build();

        User user = User.builder()
                .type("CUSTOMER")
                .tenureInYears(3)
                .build();

        Bill bill1 = Bill.builder()
                .items(List.of(item))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(user)
                .build();

        assertEquals(List.of(item), bill1.getItems());
        assertEquals(new BigDecimal("999.99"), bill1.getTotalAmount());
        assertEquals("USD", bill1.getOriginalCurrency());
        assertEquals("EUR", bill1.getTargetCurrency());
        assertEquals(user, bill1.getUser());

        Bill bill2 = new Bill();
        bill2.setItems(List.of(item));
        bill2.setTotalAmount(new BigDecimal("999.99"));
        bill2.setOriginalCurrency("USD");
        bill2.setTargetCurrency("EUR");
        bill2.setUser(user);

        assertEquals(bill1, bill2);
        assertEquals(bill1.hashCode(), bill2.hashCode());

        assertNotNull(bill1.toString());
        assertTrue(bill1.toString().contains("999.99"));
        assertTrue(bill1.toString().contains("USD"));
    }

    @Test
    public void testMultipleValidationErrors() {
        Bill bill = Bill.builder()
                .items(new ArrayList<>())
                .totalAmount(null)
                .originalCurrency("")
                .targetCurrency("")
                .user(null)
                .build();

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);
        assertEquals(5, violations.size(), "Should have five validation errors");
    }

    @Test
    public void testBuilderToString() {
        Bill.BillBuilder builder = Bill.builder()
                .items(List.of(Item.builder().name("Laptop").category("ELECTRONICS").price(new BigDecimal("999.99")).build()))
                .totalAmount(new BigDecimal("999.99"))
                .originalCurrency("USD")
                .targetCurrency("EUR")
                .user(User.builder().type("CUSTOMER").tenureInYears(3).build());

        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("BillBuilder"));
        assertTrue(builderString.contains("items="));
        assertTrue(builderString.contains("totalAmount=999.99"));
        assertTrue(builderString.contains("originalCurrency=USD"));
        assertTrue(builderString.contains("targetCurrency=EUR"));
        assertTrue(builderString.contains("user="));
    }
}