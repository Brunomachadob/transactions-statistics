package com.n26.statistics;

import com.n26.transactions.Transaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StatisticsTest {

    @Test
    public void should_successfully_compute_transaction() {
        Statistics statistics = new Statistics().addTransaction(
            new Transaction(BigDecimal.valueOf(10.0), LocalDateTime.now())
        );

        assertThat(statistics.getCount(), is(1L));
        assertThat(statistics.getMax(), is(BigDecimal.valueOf(10.0)));
        assertThat(statistics.getMin(), is(BigDecimal.valueOf(10.0)));
        assertThat(statistics.getSum(), is(BigDecimal.valueOf(10.0)));
        assertThat(statistics.getAvg(), is(BigDecimal.valueOf(10.0)));
    }

    @Test
    public void should_successfully_compute_multiple_transactions() {
        Statistics statistics = new Statistics()
            .addTransaction(
                new Transaction(BigDecimal.valueOf(10.0), LocalDateTime.now())
            )
            .addTransaction(
                new Transaction(BigDecimal.valueOf(20.0), LocalDateTime.now())
            );

        assertThat(statistics.getCount(), is(2L));
        assertThat(statistics.getMax(), is(BigDecimal.valueOf(20.0)));
        assertThat(statistics.getMin(), is(BigDecimal.valueOf(10.0)));
        assertThat(statistics.getSum(), is(BigDecimal.valueOf(30.0)));
        assertThat(statistics.getAvg(), is(BigDecimal.valueOf(15.0)));
    }

    @Test
    public void should_successfully_combine_multiple_statistics() {
        Statistics s1 = new Statistics().addTransaction(
            new Transaction(BigDecimal.valueOf(10.0), LocalDateTime.now())
        );

        Statistics s2 = new Statistics().addTransaction(
            new Transaction(BigDecimal.valueOf(20.0), LocalDateTime.now())
        );

        Statistics s3 = s2.combine(s1);

        assertThat(s3.getCount(), is(2L));
        assertThat(s3.getMax(), is(BigDecimal.valueOf(20.0)));
        assertThat(s3.getMin(), is(BigDecimal.valueOf(10.0)));
        assertThat(s3.getSum(), is(BigDecimal.valueOf(30.0)));
        assertThat(s3.getAvg(), is(BigDecimal.valueOf(15.0)));
    }
}
