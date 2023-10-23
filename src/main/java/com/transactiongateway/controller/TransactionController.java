package com.transactiongateway.controller;

import com.transactiongateway.dto.TransactionRequest;
import com.transactiongateway.dto.TransactionResponse;
import com.transactiongateway.entity.Transaction;
import com.transactiongateway.exception.TransactionNotFoundException;
import com.transactiongateway.service.ExchangeRatesService;
import com.transactiongateway.service.TransactionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private ExchangeRatesService exchangeRatesService;
    List<Transaction> transactions;
    Transaction transaction;
    @GetMapping
    public ResponseEntity<List<Transaction>> findAll(){
        return new ResponseEntity<List<Transaction>>(transactionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TransactionResponse> findById(@PathVariable final String id, @RequestParam(value = "country_currency", required = false) String countryCurrency) throws TransactionNotFoundException {
        transaction = this.transactionService.findById(id);
        TransactionResponse response = new TransactionResponse();
        if (transaction != null && countryCurrency != null) {
            BigDecimal exchangeRateValue = exchangeRatesService.findExchangeRate(transaction, countryCurrency);
            response.setPurchaseAmountConverted(exchangeRatesService.convertPurchaseAmount(transaction.getPurchaseAmount(), exchangeRateValue));

        }
        response.setId(transaction.getId());
        response.setDescription(transaction.getDescription());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setPurchaseAmount(transaction.getPurchaseAmount());

        return new ResponseEntity<TransactionResponse>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        Transaction savedTransaction = transactionService.save(transactionRequest);
        return new ResponseEntity<Transaction>(savedTransaction, HttpStatus.CREATED);
    }
}
