package com.transactiongateway.service;

import com.transactiongateway.entity.Transaction;
import com.transactiongateway.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository repository;

    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public void save(Transaction transaction){
       repository.save(transaction);
    }
}
