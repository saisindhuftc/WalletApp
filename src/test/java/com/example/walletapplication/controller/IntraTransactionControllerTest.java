package com.example.walletapplication.controller;

import com.example.walletapplication.exception.TransactionNotFoundException;
import com.example.walletapplication.responseModels.TransactionsResponseModel;
import com.example.walletapplication.service.IntraTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IntraTransactionController.class)
public class IntraTransactionControllerTest {

    @MockBean
    private IntraTransactionService intraTransactionService;

    @InjectMocks
    private IntraTransactionController intraTransactionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(intraTransactionController).build();
    }

    @Test
    public void testAllTransactionsWithoutDates() throws Exception {
        TransactionsResponseModel mockResponse = new TransactionsResponseModel();
        when(intraTransactionService.allTransactions()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(intraTransactionService, times(1)).allTransactions();
    }

    @Test
    public void testAllTransactionsWithDates() throws Exception {
        TransactionsResponseModel mockResponse = new TransactionsResponseModel();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        when(intraTransactionService.allTransactionsDateBased(startDate, endDate)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/transactions")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(intraTransactionService, times(1)).allTransactionsDateBased(startDate, endDate);
    }

    @Test
    public void testAllTransactionsTransactionNotFound() throws Exception {
        when(intraTransactionService.allTransactions()).thenThrow(new TransactionNotFoundException("Transactions not found"));

        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transactions not found"));

        verify(intraTransactionService, times(1)).allTransactions();
    }

    @Test
    public void testAllTransactionsDateBasedTransactionNotFound() throws Exception {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        when(intraTransactionService.allTransactionsDateBased(startDate, endDate)).thenThrow(new TransactionNotFoundException("Transactions not found"));

        mockMvc.perform(get("/api/v1/transactions")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transactions not found"));

        verify(intraTransactionService, times(1)).allTransactionsDateBased(startDate, endDate);
    }

    @Test
    public void testAllTransactionsGenericException() throws Exception {
        when(intraTransactionService.allTransactions()).thenThrow(new RuntimeException("An error occurred while fetching transactions"));

        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while fetching transactions"));

        verify(intraTransactionService, times(1)).allTransactions();
    }

}