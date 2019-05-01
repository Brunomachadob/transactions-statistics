package com.n26.statistics;

import com.n26.transactions.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

@Service
public class StatisticsService {

    private static final int MAX_STATISTICS_AGE_SEC = 60;

    private Map<Integer, Statistics> statisticsBySecond = new ConcurrentHashMap<>();

    public void addTransaction(Transaction transaction) {
        int transactionSecond = transaction.getTimestamp().getSecond();

        statisticsBySecond.compute(transactionSecond, (key, statisticBySecond) ->
                Optional.ofNullable(statisticBySecond)
                        .filter(isValidStatistic())
                        .orElse(new Statistics())
                        .addTransaction(transaction)
        );
    }

    public Statistics getStatistics() {
        return statisticsBySecond.values()
                .stream()
                .filter(isValidStatistic())
                .reduce(reduceStatistics())
                .orElse(new Statistics());
    }

    public void clearStatistics() {
        this.statisticsBySecond.clear();
    }

    private BinaryOperator<Statistics> reduceStatistics() {
        return (prev, curr) -> prev.combine(curr);
    }

    private Predicate<Statistics> isValidStatistic() {
        LocalDateTime expireTime = LocalDateTime
                .now()
                .minus(MAX_STATISTICS_AGE_SEC, ChronoUnit.SECONDS);

        return (statistic -> statistic.getTimestamp().isAfter(expireTime));
    }
}
