package com.billcalculationservice.Bill.Calculation.service.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUser() {
        User user = User.builder()
                .type("CUSTOMER")
                .tenureInYears(3)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Valid user should not have validation errors");
    }

    @ParameterizedTest
    @ValueSource(strings = {"EMPLOYEE", "AFFILIATE", "CUSTOMER", "GUEST"})
    public void testValidUserTypes(String type) {
        User user = User.builder()
                .type(type)
                .tenureInYears(3)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "User with type '" + type + "' should be valid");
    }

    @Test
    public void testBlankType() {
        User user = User.builder()
                .type("")
                .tenureInYears(3)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("User type is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullType() {
        User user = User.builder()
                .type(null)
                .tenureInYears(3)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("User type is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativeTenure() {
        User user = User.builder()
                .type("CUSTOMER")
                .tenureInYears(-1)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Should have one validation error");
        assertEquals("Tenure must be non-negative", violations.iterator().next().getMessage());
    }

    @Test
    public void testZeroTenure() {
        User user = User.builder()
                .type("CUSTOMER")
                .tenureInYears(0)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "User with zero tenure should be valid");
    }

    @Test
    public void testLombokMethods() {
        User user1 = User.builder()
                .type("CUSTOMER")
                .tenureInYears(3)
                .build();

        assertEquals("CUSTOMER", user1.getType());
        assertEquals(3, user1.getTenureInYears());

        User user2 = new User();
        user2.setType("CUSTOMER");
        user2.setTenureInYears(3);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        assertNotNull(user1.toString());
        assertTrue(user1.toString().contains("CUSTOMER"));
        assertTrue(user1.toString().contains("3"));
    }

    @Test
    public void testDifferentUserTypes() {
        User customer = User.builder().type("CUSTOMER").tenureInYears(3).build();
        User employee = User.builder().type("EMPLOYEE").tenureInYears(1).build();
        User affiliate = User.builder().type("AFFILIATE").tenureInYears(2).build();
        User guest = User.builder().type("GUEST").tenureInYears(0).build();

        assertTrue(validator.validate(customer).isEmpty());
        assertTrue(validator.validate(employee).isEmpty());
        assertTrue(validator.validate(affiliate).isEmpty());
        assertTrue(validator.validate(guest).isEmpty());

        assertNotEquals(customer, employee);
        assertNotEquals(customer, affiliate);
        assertNotEquals(customer, guest);
        assertNotEquals(employee, affiliate);
        assertNotEquals(employee, guest);
        assertNotEquals(affiliate, guest);
    }

    @Test
    public void testMultipleValidationErrors() {
        User user = User.builder()
                .type("")
                .tenureInYears(-1)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Should have two validation errors");
    }

    @Test
    public void testBuilderToString() {
        User.UserBuilder builder = User.builder()
                .type("CUSTOMER")
                .tenureInYears(3);

        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("UserBuilder"));
        assertTrue(builderString.contains("type=CUSTOMER"));
        assertTrue(builderString.contains("tenureInYears=3"));
    }
}