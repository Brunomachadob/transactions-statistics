package com.n26.statistics;

import com.n26.transactions.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @Before
    public void setup() {
        this.statisticsService = new StatisticsService();
    }

    @Test
    public void should_get_empty_statistics() {
        Statistics statistics = statisticsService.getStatistics();

        assertThat(statistics.getCount(), is(0L));
        assertThat(statistics.getMax(), is(BigDecimal.valueOf(0)));
        assertThat(statistics.getMin(), is(BigDecimal.valueOf(0)));
        assertThat(statistics.getSum(), is(BigDecimal.valueOf(0)));
        assertThat(statistics.getAvg(), is(BigDecimal.valueOf(0)));
    }

    @Test
    public void should_add_transaction() {
       this.statisticsService.addTransaction(
           new Transaction(BigDecimal.valueOf(10.0), LocalDateTime.now())
       );

       Statistics statistics = statisticsService.getStatistics();

       assertThat(statistics.getCount(), is(1L));
       assertThat(statistics.getMax(), is(BigDecimal.valueOf(10.0)));
       assertThat(statistics.getMin(), is(BigDecimal.valueOf(10.0)));
       assertThat(statistics.getSum(), is(BigDecimal.valueOf(10.0)));
       assertThat(statistics.getAvg(), is(BigDecimal.valueOf(10.0)));
    }

    @Test
    public void should_add_multiple_transactions() {
        this.statisticsService.addTransaction(
            new Transaction(BigDecimal.valueOf(20.0), LocalDateTime.now())
        );

        this.statisticsService.addTransaction(
            new Transaction(BigDecimal.valueOf(10.0), LocalDateTime.now())
        );

        Statistics statistics = statisticsService.getStatistics();


        assertThat(statistics.getCount(), is(2L));
        assertThat(statistics.getMax(), is(BigDecimal.valueOf(20.0)));
        assertThat(statistics.getMin(), is(BigDecimal.valueOf(10.0)));
        assertThat(statistics.getSum(), is(BigDecimal.valueOf(30.0)));
        assertThat(statistics.getAvg(), is(BigDecimal.valueOf(15.0)));
    }

    @Test
    public void should_clear_statistics() {
        this.statisticsService.addTransaction(
            new Transaction(BigDecimal.valueOf(10.0), LocalDateTime.now())
        );

        assertThat(statisticsService.getStatistics().getCount(), is(1L));

        statisticsService.clearStatistics();

        assertThat(statisticsService.getStatistics().getCount(), is(0L));
    }
}
