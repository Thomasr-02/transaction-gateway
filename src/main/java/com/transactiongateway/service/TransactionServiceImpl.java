package com.transactiongateway.service;

import com.transactiongateway.dto.TransactionRequest;
import com.transactiongateway.entity.Transaction;
import com.transactiongateway.exception.TransactionNotFoundException;
import com.transactiongateway.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository repository;

    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Transaction findById(String id) throws TransactionNotFoundException {
        Transaction transaction = repository.findById(id);
        if(transaction == null){
            throw new TransactionNotFoundException("Transaction ID does not exists!");
        }
        return transaction;
    }

    public Transaction save(TransactionRequest transactionRequest){
        Transaction transaction = new Transaction();
        transaction.setPurchaseAmount(transactionRequest.getPurchaseAmount());
        transaction.setDescription(transactionRequest.getDescription());
        return repository.save(transaction);
    }
}
