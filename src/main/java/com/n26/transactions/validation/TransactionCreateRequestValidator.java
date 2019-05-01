package com.n26.transactions.validation;

import com.n26.transactions.TransactionCreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TransactionCreateRequestValidator implements Validator {

    private static final int MAX_TRANSACTION_AGE_SEC = 60;

    private final SpringValidatorAdapter validator;

    public TransactionCreateRequestValidator(SpringValidatorAdapter validator) {
        super();
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TransactionCreateRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        this.validator.validate(o, errors);

        if (errors.hasErrors()) {
            return;
        }

        TransactionCreateRequest transaction = (TransactionCreateRequest) o;

        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDateTime expireLimit = now.minus(MAX_TRANSACTION_AGE_SEC, ChronoUnit.SECONDS);

        LocalDateTime timestamp = transaction.getTimestamp().withNano(0);

        if (timestamp.isAfter(now)) {
            throw new FutureTransactionException();
        } else if (timestamp.isEqual(expireLimit) || timestamp.isBefore(expireLimit)) {
            throw new TransactionTooOldException();
        }
    }
}
