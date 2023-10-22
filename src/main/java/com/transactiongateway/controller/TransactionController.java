package com.transactiongateway.controller;

import com.transactiongateway.entity.Transaction;
import com.transactiongateway.exception.ValidationException;
import com.transactiongateway.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {
    @Autowired
    private TransactionServiceImpl service;

    @GetMapping
    public ResponseEntity<List<Transaction>> findAll(){
        return new ResponseEntity<List<Transaction>>(this.service.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable final String id){
        return new ResponseEntity<Transaction>(this.service.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> save(@RequestBody @Valid Transaction transaction, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("; "));

            throw new ValidationException("Validation failed: " + errorMessages);

        }
        this.service.save(transaction);
        return new ResponseEntity<Transaction>(HttpStatus.OK);
    }
}
