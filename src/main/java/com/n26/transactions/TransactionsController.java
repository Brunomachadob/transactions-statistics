package com.n26.transactions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.statistics.StatisticsService;
import com.n26.transactions.validation.FutureTransactionException;
import com.n26.transactions.validation.TransactionCreateRequestValidator;
import com.n26.transactions.validation.TransactionTooOldException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;

@RestController()
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private TransactionCreateRequestValidator transactionCreateRequestValidator;

    @PostMapping
    public ResponseEntity addTransaction(@Valid @RequestBody TransactionCreateRequest transactionReq, Errors errors) {

        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.statisticsService.addTransaction(transactionReq.getTransaction());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTransactions() {
        this.statisticsService.clearStatistics();
    }


    @InitBinder("transactionCreateRequest")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(this.transactionCreateRequestValidator);
    }

    @ExceptionHandler(TransactionTooOldException.class)
    public ResponseEntity handleTransactionTooOld() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(FutureTransactionException.class)
    public ResponseEntity handleFutureTransactionExc() {
        return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable rootCause = ExceptionUtils.getRootCause(e);

        if (rootCause instanceof DateTimeParseException || rootCause instanceof InvalidFormatException) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }



}
