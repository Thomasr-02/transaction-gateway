package com.transactiongateway.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class TransactionRequest {
    @NotNull(message = "Description is required")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    @NotNull(message = "username shouldn't be null")
    private String description;
    @NotNull(message = "Purchase amount is required")
    @DecimalMin(value = "0.01", message = "Purchase amount must be a positive amount")
    @DecimalMax(value = "9999999.99", message = "Purchase amount is too large")
    private BigDecimal purchaseAmount;
}
