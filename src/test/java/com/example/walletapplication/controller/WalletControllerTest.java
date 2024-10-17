package com.example.walletapplication.controller;

import com.example.walletapplication.requestDTO.WalletRequestModel;
import com.example.walletapplication.responseModels.WalletResponseModel;
import com.example.walletapplication.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @MockBean
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testDepositSuccess() throws Exception {
        WalletRequestModel requestModel = new WalletRequestModel();
        WalletResponseModel responseModel = new WalletResponseModel();
        when(walletService.deposit(anyInt(), anyString(), any(WalletRequestModel.class))).thenReturn(responseModel);

        mockMvc.perform(post("/api/v1/wallets/1/intra-wallet-transaction")
                        .header("type", "deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"money\": {\"amount\": 100, \"currency\": \"USD\"}}"))
                .andExpect(status().isAccepted());

        verify(walletService, times(1)).deposit(anyInt(), anyString(), any(WalletRequestModel.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testWithdrawSuccess() throws Exception {
        WalletRequestModel requestModel = new WalletRequestModel();
        WalletResponseModel responseModel = new WalletResponseModel();
        when(walletService.withdraw(anyInt(), anyString(), any(WalletRequestModel.class))).thenReturn(responseModel);

        mockMvc.perform(post("/api/v1/wallets/1/intra-wallet-transaction")
                        .header("type", "withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"money\": {\"amount\": 50, \"currency\": \"USD\"}}"))
                .andExpect(status().isAccepted());

        verify(walletService, times(1)).withdraw(anyInt(), anyString(), any(WalletRequestModel.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetAllWallets() throws Exception {
        List<WalletResponseModel> responseModels = new ArrayList<>();
        when(walletService.getAllWallets()).thenReturn(responseModels);

        mockMvc.perform(get("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(walletService, times(1)).getAllWallets();
    }
}
