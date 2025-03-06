package com.billcalculationservice.Bill.Calculation.service.service;

import com.billcalculationservice.Bill.Calculation.service.dto.request.Bill;
import com.billcalculationservice.Bill.Calculation.service.dto.request.Item;
import com.billcalculationservice.Bill.Calculation.service.dto.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    private Item electronicItem;
    private Item groceryItem;
    private Item clothingItem;

    @BeforeEach
    void setUp() {
        electronicItem = Item.builder()
                .name("Laptop")
                .category("ELECTRONICS")
                .price(new BigDecimal("1000.00"))
                .build();

        groceryItem = Item.builder()
                .name("Apple")
                .category("GROCERY")
                .price(new BigDecimal("2.00"))
                .build();

        clothingItem = Item.builder()
                .name("T-Shirt")
                .category("CLOTHING")
                .price(new BigDecimal("20.00"))
                .build();
    }

    @Test
    void calculateDiscount_WithEmployeeDiscount_ReturnsCorrectDiscount() {
        User employee = User.builder()
                .type("EMPLOYEE")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(electronicItem, groceryItem);
        BigDecimal totalAmount = new BigDecimal("1002.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(employee)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("335.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithAffiliateDiscount_ReturnsCorrectDiscount() {
        User affiliate = User.builder()
                .type("AFFILIATE")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(electronicItem, groceryItem);
        BigDecimal totalAmount = new BigDecimal("1002.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(affiliate)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("145.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithLoyalCustomerDiscount_ReturnsCorrectDiscount() {
        User loyalCustomer = User.builder()
                .type("CUSTOMER")
                .tenureInYears(3)
                .build();

        List<Item> items = List.of(electronicItem, groceryItem);
        BigDecimal totalAmount = new BigDecimal("1002.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(loyalCustomer)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);


        BigDecimal expectedDiscount = new BigDecimal("95.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithNonLoyalCustomer_ReturnsOnlyAmountBasedDiscount() {
        User newCustomer = User.builder()
                .type("CUSTOMER")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(electronicItem, groceryItem);
        BigDecimal totalAmount = new BigDecimal("1002.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(newCustomer)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);


        BigDecimal expectedDiscount = new BigDecimal("50.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithGuestUser_ReturnsOnlyAmountBasedDiscount() {
        User guest = User.builder()
                .type("GUEST")
                .tenureInYears(0)
                .build();

        List<Item> items = List.of(electronicItem, groceryItem);
        BigDecimal totalAmount = new BigDecimal("1002.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(guest)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("50.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithOnlyGroceryItems_ReturnsOnlyAmountBasedDiscount() {
        User employee = User.builder()
                .type("EMPLOYEE")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(
                Item.builder().name("Apple").category("GROCERY").price(new BigDecimal("50.00")).build(),
                Item.builder().name("Banana").category("GROCERY").price(new BigDecimal("30.00")).build(),
                Item.builder().name("Milk").category("GROCERY").price(new BigDecimal("20.00")).build()
        );
        BigDecimal totalAmount = new BigDecimal("100.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(employee)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("5.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithSmallAmount_ReturnsOnlyPercentageDiscount() {
        User employee = User.builder()
                .type("EMPLOYEE")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(
                Item.builder().name("Small Item").category("ELECTRONICS").price(new BigDecimal("50.00")).build()
        );
        BigDecimal totalAmount = new BigDecimal("50.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(employee)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("15.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithMixedItems_ReturnsCorrectDiscount() {
        User affiliate = User.builder()
                .type("AFFILIATE")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(
                electronicItem,
                groceryItem,
                clothingItem
        );
        BigDecimal totalAmount = new BigDecimal("1022.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(affiliate)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("147.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithBillMismatchingTotalAmount_ThrowsException() {
        User customer = User.builder()
                .type("CUSTOMER")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(electronicItem, groceryItem); // Total 1002
        BigDecimal incorrectTotal = new BigDecimal("1000.00"); // Different from sum of items

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(incorrectTotal) // Incorrect total
                .user(customer)
                .build();


        Exception exception = assertThrows(RuntimeException.class, () -> {
            discountService.calculateDiscount(bill);
        });

        assertEquals("Total amount is different from Item total amount", exception.getMessage());
    }

    @Test
    void calculateDiscount_WithEmptyItemsList_ReturnsZeroDiscount() {
        User customer = User.builder()
                .type("CUSTOMER")
                .tenureInYears(1)
                .build();

        List<Item> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(customer)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), discount);
    }

    @Test
    void calculateDiscount_WithExactMultipleOf100_ReturnsCorrectDiscount() {
        User customer = User.builder()
                .type("CUSTOMER")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(
                Item.builder().name("Item").category("ELECTRONICS").price(new BigDecimal("500.00")).build()
        );
        BigDecimal totalAmount = new BigDecimal("500.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(customer)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("25.00");
        assertEquals(expectedDiscount, discount);
    }

    @Test
    void calculateDiscount_WithCaseInsensitiveGroceryCategory_ReturnsCorrectDiscount() {
        User employee = User.builder()
                .type("EMPLOYEE")
                .tenureInYears(1)
                .build();

        List<Item> items = List.of(
                electronicItem,
                Item.builder().name("Mixed Case").category("GrOcErY").price(new BigDecimal("100.00")).build()
        );
        BigDecimal totalAmount = new BigDecimal("1100.00");

        Bill bill = Bill.builder()
                .items(items)
                .totalAmount(totalAmount)
                .user(employee)
                .build();

        BigDecimal discount = discountService.calculateDiscount(bill);

        BigDecimal expectedDiscount = new BigDecimal("340.00");
        assertEquals(expectedDiscount, discount);
    }
}
