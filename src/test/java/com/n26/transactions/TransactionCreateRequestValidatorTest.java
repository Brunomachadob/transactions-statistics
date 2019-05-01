package com.n26.transactions;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import com.n26.transactions.validation.FutureTransactionException;
import com.n26.transactions.validation.TransactionCreateRequestValidator;
import com.n26.transactions.validation.TransactionTooOldException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionCreateRequestValidatorTest {

    @Autowired
    TransactionCreateRequestValidator transactionCreateRequestValidator;

    @Test
    public void should_add_transaction() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAmount(BigDecimal.ONE);
        request.setTimestamp(LocalDateTime.now());

        BindException errors = new BindException(request, "TransactionCreateRequest");
        transactionCreateRequestValidator.validate(request, errors);

        assertThat(errors.hasErrors(), is(false));
    }

    @Test
    public void should_fail_with_null_amount() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setTimestamp(LocalDateTime.now());

        BindException errors = new BindException(request, "TransactionCreateRequest");
        transactionCreateRequestValidator.validate(request, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldError("amount").getRejectedValue(), nullValue());
    }

    @Test
    public void should_fail_with_invalid_amount() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAmount(BigDecimal.ONE.negate());
        request.setTimestamp(LocalDateTime.now());

        BindException errors = new BindException(request, "TransactionCreateRequest");
        transactionCreateRequestValidator.validate(request, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldError("amount").getRejectedValue(), is(BigDecimal.ONE.negate()));
    }

    @Test
    public void should_fail_with_null_timestamp() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAmount(BigDecimal.ONE);

        BindException errors = new BindException(request, "TransactionCreateRequest");
        transactionCreateRequestValidator.validate(request, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldError("timestamp").getRejectedValue(), nullValue());
    }

    @Test(expected = TransactionTooOldException.class)
    public void should_fail_with_too_old_timestamp() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAmount(BigDecimal.ONE);
        request.setTimestamp(LocalDateTime.now().minus(61, ChronoUnit.SECONDS));

        BindException errors = new BindException(request, "TransactionCreateRequest");
        transactionCreateRequestValidator.validate(request, errors);
    }

    @Test(expected = FutureTransactionException.class)
    public void should_fail_with_future_timestamp() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAmount(BigDecimal.ONE);
        request.setTimestamp(LocalDateTime.now().plus(1, ChronoUnit.SECONDS));

        BindException errors = new BindException(request, "TransactionCreateRequest");
        transactionCreateRequestValidator.validate(request, errors);
    }
}
