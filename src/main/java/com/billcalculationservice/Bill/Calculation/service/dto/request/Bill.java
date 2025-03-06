package com.billcalculationservice.Bill.Calculation.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request containing bill details for calculation")
public class Bill {

    @NotEmpty(message = "Items list cannot be empty")
    private List<@Valid Item> items;

    @NotNull(message = "Total amount cannot be null")
    @DecimalMin(value = "0.0", message = "Total amount must be non-negative")
    private BigDecimal totalAmount;

    @NotBlank(message = "Original currency is required")
    private String originalCurrency;

    @NotBlank(message = "Target currency is required")
    private String targetCurrency;

    @NotNull(message = "User information is required")
    private @Valid User user;
}