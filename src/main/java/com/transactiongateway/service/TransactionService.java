package com.transactiongateway.service;

import com.transactiongateway.dto.TransactionRequest;
import com.transactiongateway.entity.Transaction;
import com.transactiongateway.exception.TransactionNotFoundException;

import java.util.List;

public interface TransactionService {
    List<Transaction> findAll();

    Transaction findById(String id) throws TransactionNotFoundException;
    Transaction save(TransactionRequest transaction);
}
