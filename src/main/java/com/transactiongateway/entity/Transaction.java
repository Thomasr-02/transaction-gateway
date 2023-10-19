package com.transactiongateway.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_date")
    private Date transaction_date;
    @Column(name = "purcahse_amount")
    private String purchase_amount;
}
