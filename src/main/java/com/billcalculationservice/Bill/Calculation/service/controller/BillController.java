package com.billcalculationservice.Bill.Calculation.service.controller;

import com.billcalculationservice.Bill.Calculation.service.dto.request.Bill;
import com.billcalculationservice.Bill.Calculation.service.dto.response.BillResponse;
import com.billcalculationservice.Bill.Calculation.service.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bill Calculator")
public class BillController {

    private final BillService billService;

    @Operation(summary = "Calculate Payable Amount")
    @PostMapping("/calculate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BillResponse> calculatePayableAmount(@Valid @RequestBody Bill requestDTO) {

        BillResponse billResponse = billService.calculatePayableAmount(requestDTO);

        return ResponseEntity.ok(billResponse);
    }
}