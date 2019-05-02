package com.n26.statistics;

import com.n26.transactions.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
public class Statistics {
    private static final MathContext MATH_CTX = new MathContext(4, RoundingMode.HALF_UP);

    private LocalDateTime timestamp = LocalDateTime.MIN;

    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal max;
    private BigDecimal min;
    private long count;

    public BigDecimal getAvg() {
        return this.count == 0 ?
                BigDecimal.ZERO :
                this.getSum().divide(BigDecimal.valueOf(this.getCount()), MATH_CTX);
    }

    public BigDecimal getMax() {
        return max == null ? BigDecimal.ZERO : max;
    }

    public BigDecimal getMin() {
        return min == null ? BigDecimal.ZERO : min;
    }

    public Statistics combine(Statistics other) {
        Statistics newStatistics = new Statistics();

        newStatistics.setCount(this.count + other.getCount());

        newStatistics.setTimestamp(
            this.timestamp.isBefore(other.getTimestamp()) ? other.getTimestamp() : this.timestamp
        );

        newStatistics.setSum(this.sum.add(other.getSum()));

        newStatistics.setMin(this.min == null ? other.getMin() : this.min.min(other.getMin()));
        newStatistics.setMax(this.max == null ? other.getMax() : this.max.max(other.getMax()));

        return newStatistics;
    }

    /*
     * Maybe this is not the best approach but it helped me having a single point
     * of calculation, in the `combine`.
     */
    public Statistics addTransaction(Transaction transaction) {
        Statistics newStatistics = new Statistics();

        newStatistics.setCount(1L);
        newStatistics.setTimestamp(transaction.getTimestamp());
        newStatistics.setSum(transaction.getAmount());
        newStatistics.setMin(transaction.getAmount());
        newStatistics.setMax(transaction.getAmount());

        return this.combine(newStatistics);
    }
}
