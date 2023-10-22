package com.transactiongateway.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
@Table(name = "gateway_transaction")
public class Transaction implements Serializable {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "description")
    @NotNull(message = "Description is required")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @Column(name = "transaction_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionDate;

    @Column(name = "purchase_amount")
    @NotNull(message = "Purchase amount is required")
    @DecimalMin(value = "0.01", message = "Purchase amount must be a positive amount")
    @DecimalMax(value = "9999999.99", message = "Purchase amount is too large")
    private BigDecimal purchaseAmount;

    @PrePersist
    public void prePersist() {
        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }
    }
}
