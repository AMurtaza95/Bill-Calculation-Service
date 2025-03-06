package com.billcalculationservice.Bill.Calculation.service.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidItem() {
        Item item = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"))
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertTrue(violations.isEmpty(), "Valid item should not have validation errors");
    }

    @Test
    public void testBlankName() {
        Item item = Item.builder()
                .name("")
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"))
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Item name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullName() {
        Item item = Item.builder()
                .name(null)
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"))
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Item name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testBlankCategory() {
        Item item = Item.builder()
                .name("Laptop")
                .category("")
                .price(new BigDecimal("999.99"))
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Category is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullCategory() {
        Item item = Item.builder()
                .name("Laptop")
                .category(null)
                .price(new BigDecimal("999.99"))
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Category is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testZeroPrice() {
        Item item = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(BigDecimal.ZERO)
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Price must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativePrice() {
        Item item = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("-10.00"))
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Price must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullPrice() {
        Item item = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(null)
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Price is required", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"GROCERY", "grocery", "Grocery"})
    public void testIsGroceryTrue(String category) {
        Item item = Item.builder()
                .name("Apple")
                .category(category)
                .price(new BigDecimal("1.99"))
                .build();

        assertTrue(item.isGrocery(), "Item with category '" + category + "' should be identified as grocery");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ELECTRONICS", "CLOTHING", "OTHER"})
    public void testIsGroceryFalse(String category) {
        Item item = Item.builder()
                .name("Laptop")
                .category(category)
                .price(new BigDecimal("999.99"))
                .build();

        assertFalse(item.isGrocery(), "Item with category '" + category + "' should not be identified as grocery");
    }

    @Test
    public void testLombokMethods() {
        Item item1 = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"))
                .build();

        assertEquals("Laptop", item1.getName());
        assertEquals("ELECTRONICS", item1.getCategory());
        assertEquals(new BigDecimal("999.99"), item1.getPrice());

        Item item2 = new Item();
        item2.setName("Laptop");
        item2.setCategory("ELECTRONICS");
        item2.setPrice(new BigDecimal("999.99"));

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());

        assertNotNull(item1.toString());
        assertTrue(item1.toString().contains("Laptop"));
        assertTrue(item1.toString().contains("ELECTRONICS"));
        assertTrue(item1.toString().contains("999.99"));
    }

    @Test
    public void testMultipleValidationErrors() {
        Item item = Item.builder()
                .name("")
                .category("")
                .price(new BigDecimal("-10.00"))
                .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(3, violations.size(), "Should have three validation errors");
    }

    @Test
    public void testBuilderToString() {
        Item.ItemBuilder builder = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("999.99"));

        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("ItemBuilder"));
        assertTrue(builderString.contains("name=Laptop"));
        assertTrue(builderString.contains("category=ELECTRONICS"));
        assertTrue(builderString.contains("price=999.99"));
    }
}