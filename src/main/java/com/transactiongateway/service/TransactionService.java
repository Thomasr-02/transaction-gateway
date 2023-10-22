package com.transactiongateway.service;

import com.transactiongateway.entity.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> findAll();

    Transaction findById(String id);
    void save(Transaction transaction);
}
