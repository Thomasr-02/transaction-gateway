package com.transactiongateway.controller;

import com.transactiongateway.entity.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {
    @GetMapping
    public ResponseEntity<List<Transaction>> getAll(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
