package com.transactiongateway.controller;

import com.transactiongateway.entity.Transaction;
import com.transactiongateway.exception.ValidationException;
import com.transactiongateway.service.ExchangeRatesService;
import com.transactiongateway.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable final String id, @RequestParam(value = "country_currency", required = false) String countryCurrency){
        transaction = this.transactionService.findById(id);
        if (transaction != null && countryCurrency != null) {
            BigDecimal exchangeRateValue = exchangeRatesService.findExchangeRate(transaction, countryCurrency);
            transaction.setPurchaseAmountConverted(exchangeRatesService.convertPurchaseAmount(transaction.getPurchaseAmount(), exchangeRateValue));
        }
        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> save(@RequestBody @Valid Transaction transaction, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("; "));

            throw new ValidationException("Validation failed: " + errorMessages);

        }
        this.transactionService.save(transaction);
        return new ResponseEntity<Transaction>(HttpStatus.OK);
    }
}
