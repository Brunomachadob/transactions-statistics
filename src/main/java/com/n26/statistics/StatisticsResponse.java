package com.n26.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StatisticsResponse {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;

    public static StatisticsResponse fromStatistics(Statistics statistics) {
        return new StatisticsResponse(
                statistics.getSum(),
                statistics.getAvg(),
                statistics.getMax(),
                statistics.getMin(),
                statistics.getCount()
        );
    }
}
