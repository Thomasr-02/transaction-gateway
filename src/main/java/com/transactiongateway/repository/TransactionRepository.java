package com.transactiongateway.repository;

import com.transactiongateway.dto.TransactionRequest;
import com.transactiongateway.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<Transaction, Long> {
    List<Transaction> findAll();

    Transaction findById(String id);
    Transaction save(Transaction transaction);
}
