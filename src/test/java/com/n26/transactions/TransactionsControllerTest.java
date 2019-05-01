package com.n26.transactions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.statistics.StatisticsService;
import com.n26.transactions.validation.TransactionCreateRequestValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionsController.class)
public class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private StatisticsService statisticsService;

    @MockBean
    TransactionCreateRequestValidator transactionCreateRequestValidator;

    @Test
    public void should_clear_transactions() throws Exception {
        StatisticsService spy = Mockito.spy(statisticsService);
        Mockito.doNothing().when(spy).clearStatistics();

        this.mockMvc.perform(delete("/transactions"))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    public void should_add_transaction() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAmount(BigDecimal.ONE);
        request.setTimestamp(LocalDateTime.now());

        when(transactionCreateRequestValidator.supports(TransactionCreateRequest.class)).thenReturn(true);

        StatisticsService spy = Mockito.spy(statisticsService);
        Mockito.doNothing().when(spy).addTransaction(Mockito.any(Transaction.class));

        this.mockMvc.perform(
            post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(print())
            .andExpect(status().isCreated());
    }
}
