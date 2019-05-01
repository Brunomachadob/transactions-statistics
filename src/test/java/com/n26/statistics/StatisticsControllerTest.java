package com.n26.statistics;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    public void should_return_statistics() throws Exception {
        Statistics statistics = new Statistics();
        statistics.setCount(1L);
        statistics.setMin(BigDecimal.TEN);
        statistics.setMax(BigDecimal.TEN);
        statistics.setSum(BigDecimal.TEN);

        when(statisticsService.getStatistics()).thenReturn(statistics);

        this.mockMvc.perform(get("/statistics"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("count").value(1))
                .andExpect(jsonPath("min").value("10.00"))
                .andExpect(jsonPath("max").value("10.00"))
                .andExpect(jsonPath("sum").value("10.00"))
                .andExpect(jsonPath("avg").value("10.00"));
    }

//    @Test
//    public void should_clear_statistics() throws Exception {
//        StatisticsService spy = Mockito.spy(statisticsService);
//        Mockito.doNothing().when(spy).clearStatistics();
//
//        this.mockMvc.perform(delete("/transactions"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}
