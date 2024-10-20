package com.example.walletapplication.controller;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.requestDTO.InterWalletTransactionRequestDTO;
import com.example.walletapplication.service.InterWalletTransactionService;
import com.example.walletapplication.service.IntraWalletTransactionService;
import com.example.walletapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InterWalletTransactionControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private InterWalletTransactionService interWalletTransactionService;

    @Mock
    private IntraWalletTransactionService intraWalletTransactionService;

    @InjectMocks
    private InterWalletTransactionController interWalletTransactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(interWalletTransactionController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser
    void testTransferSuccess() throws Exception {
        InterWalletTransactionRequestDTO requestDTO = new InterWalletTransactionRequestDTO(1L, 2L, 100.0, CurrencyType.USD);

        doNothing().when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));
        doNothing().when(intraWalletTransactionService).isUserAuthorized(anyLong(), anyLong());

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transfer successful"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
        verify(intraWalletTransactionService, times(1)).isUserAuthorized(1L, 1L);
    }

    @Test
    @WithMockUser
    void testTransferSenderNotFoundException1() throws Exception {
        doThrow(new UserNotFoundException("Sender not found"))
                .when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Sender not found"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    void testTransferReceiverNotFoundException() throws Exception {
        doThrow(new UserNotFoundException("Receiver not found")).when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Receiver not found"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    void testTransferUnAuthorisedUserException() throws Exception {
        doThrow(new UnAuthorisedUserException("User not authorized")).when(intraWalletTransactionService).isUserAuthorized(anyLong(), anyLong());

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized"))
                .andDo(print());

        verify(intraWalletTransactionService, times(1)).isUserAuthorized(1L, 1L);
    }

    @Test
    @WithMockUser
    void testTransferUnAuthorisedWalletException() throws Exception {
        doThrow(new UnAuthorisedWalletException("User not authorized for this wallet")).when(intraWalletTransactionService).isUserAuthorized(anyLong(), anyLong());

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized for this wallet"))
                .andDo(print());

        verify(intraWalletTransactionService, times(1)).isUserAuthorized(1L, 1L);
    }

    @Test
    @WithMockUser
    void testTransferInvalidCurrencyException() throws Exception {
        doThrow(new InvalidCurrencyException("Invalid currency")).when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid currency"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    void testTransferInsufficientBalanceException() throws Exception {
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient balance"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    void testTransferInvalidAmountException() throws Exception {
        doThrow(new InvalidAmountException("Invalid amount")).when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid amount"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    void testTransferFailedException() throws Exception {
        doThrow(new DepositFailedException("Deposit failed")).when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Deposit failed"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    void testTransferWalletNotFoundException() throws Exception {
        doThrow(new WalletNotFoundException("Wallet not found")).when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wallet not found"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    void testTransferUserAndWalletMismatchException() throws Exception {
        doThrow(new UserAndWalletMismatchException("User and wallet mismatch")).when(intraWalletTransactionService).isUserAuthorized(anyLong(), anyLong());

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User and wallet mismatch"))
                .andDo(print());

        verify(intraWalletTransactionService, times(1)).isUserAuthorized(1L, 1L);
    }

    @Test
    @WithMockUser
    void testTransferInternalServerException() throws Exception {
        doThrow(new RuntimeException("Internal server error")).when(interWalletTransactionService).transfer(anyLong(), anyLong(), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderWalletId\":1,\"receiverWalletId\":2,\"amount\":100.0,\"currency\":\"USD\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andDo(print());

        verify(interWalletTransactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
    }
}