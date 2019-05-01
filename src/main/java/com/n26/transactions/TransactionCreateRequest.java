package com.n26.transactions;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionCreateRequest {

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @NotNull
    @DateTimeFormat(pattern = "YYYY-MM-DDThh:mm:ss.sssZ")
    private LocalDateTime timestamp;


    public Transaction getTransaction() {
        return new Transaction(amount, timestamp);
    }
}
