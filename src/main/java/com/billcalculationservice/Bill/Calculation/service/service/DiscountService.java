package com.billcalculationservice.Bill.Calculation.service.service;


import com.billcalculationservice.Bill.Calculation.service.dto.request.Bill;
import com.billcalculationservice.Bill.Calculation.service.dto.request.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
public class DiscountService {

    private static final BigDecimal EMPLOYEE_DISCOUNT = new BigDecimal("0.30");
    private static final BigDecimal AFFILIATE_DISCOUNT = new BigDecimal("0.10");
    private static final BigDecimal LOYAL_CUSTOMER_DISCOUNT = new BigDecimal("0.05");
    private static final int LOYAL_CUSTOMER_YEARS = 2;
    private static final BigDecimal AMOUNT_PER_DISCOUNT = new BigDecimal("100.0");
    private static final BigDecimal DISCOUNT_PER_AMOUNT = new BigDecimal("5.0");
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final int SCALE = 2;

    public BigDecimal calculateDiscount(Bill bill) {
        List<Item> items = bill.getItems();

        BigDecimal totalAmount = items.stream()
                .map(Item::getPrice)
                .reduce(ZERO, BigDecimal::add);

        if(!totalAmount.equals(bill.getTotalAmount())) {
            throw new RuntimeException("Total amount is different from Item total amount");
        }

        log.info("Total amount: {}", totalAmount);

        BigDecimal groceryAmount = items.stream()
                .filter(Item::isGrocery)
                .map(Item::getPrice)
                .reduce(ZERO, BigDecimal::add);

        log.info("Grocery amount: {}", groceryAmount);

        BigDecimal percentageDiscount = getPercentageDiscount(bill, totalAmount, groceryAmount);

        log.info("Percentage discount: {}", percentageDiscount);

        BigDecimal amountAfterPercentageDiscount = totalAmount.subtract(percentageDiscount);

        log.info("Amount after percentage discount: {}", amountAfterPercentageDiscount);

        BigDecimal discountMultiplier = amountAfterPercentageDiscount.divideToIntegralValue(AMOUNT_PER_DISCOUNT);

        log.info("Discount multiplier: {}", discountMultiplier);

        BigDecimal amountBasedDiscount = discountMultiplier.multiply(DISCOUNT_PER_AMOUNT);

        log.info("Amount based discount: {}", amountBasedDiscount);

        return percentageDiscount.add(amountBasedDiscount).setScale(SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal getPercentageDiscount(Bill bill, BigDecimal totalAmount, BigDecimal groceryAmount) {
        BigDecimal nonGroceryAmount = totalAmount.subtract(groceryAmount);

        log.info("Non-grocery amount: {}", nonGroceryAmount);

        BigDecimal percentageDiscount = ZERO;

        if (nonGroceryAmount.compareTo(ZERO) > 0) {
            String userType = bill.getUser().getType();
            int tenure = bill.getUser().getTenureInYears();

            if ("EMPLOYEE".equals(userType)) {
                percentageDiscount = nonGroceryAmount.multiply(EMPLOYEE_DISCOUNT);
            } else if ("AFFILIATE".equals(userType)) {
                percentageDiscount = nonGroceryAmount.multiply(AFFILIATE_DISCOUNT);
            } else if ("CUSTOMER".equals(userType) && tenure > LOYAL_CUSTOMER_YEARS) {
                percentageDiscount = nonGroceryAmount.multiply(LOYAL_CUSTOMER_DISCOUNT);
            }
        }
        return percentageDiscount;
    }
}
