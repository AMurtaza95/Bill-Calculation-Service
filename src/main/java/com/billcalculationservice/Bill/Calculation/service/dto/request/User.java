package com.billcalculationservice.Bill.Calculation.service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotBlank(message = "User type is required")
    private String type;

    @Min(value = 0, message = "Tenure must be non-negative")
    private int tenureInYears;
}