package com.n26.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Transaction {
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
