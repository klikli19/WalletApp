package com.klikli.walletapp;

import static java.lang.reflect.Array.get;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klikli.walletapp.controller.WalletController;
import com.klikli.walletapp.dto.WalletRequest;
import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.service.WalletService;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@WebMvcTest(controllers = WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private WalletService service;

    @InjectMocks
    private WalletController walletController;

    private UUID walletId;
    private Wallet wallet;
    @Autowired
    private AssertTrueValidator assertTrue;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        walletId = UUID.randomUUID();
        wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(100L);
    }

    @Test
    public void testUpdateWallet_Deposit_Success() throws Exception {
        WalletRequest request = new WalletRequest();
        when(service.updateBalance(eq(walletId), eq("DEPOSIT"), eq(50.0))).thenReturn(wallet);
        mockMvc.perform(post("/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    public void testUpdateWallet_Withdraw_Success() throws Exception {

        WalletRequest request = new WalletRequest();
        when(service.updateBalance(eq(walletId), eq("WITHDRAW"), eq(30.0))).thenReturn(wallet);


        mockMvc.perform(post("/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    public void testUpdateWallet_Withdraw_InsufficientFunds() throws Exception {
        // Arrange
        WalletRequest request =new WalletRequest(); // Запрашиваемая сумма больше баланса
        when(service.updateBalance(eq(walletId), eq("WITHDRAW"), eq(150.0)))
                .thenThrow(new IllegalArgumentException("Недостаточно средств"));


        mockMvc.perform(post("/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals("Недостаточно средств", result.getResolvedException().getMessage()));
    }
    @Test
    public void testGetWallet_Success() throws Exception {
        when(service.getBalance(walletId)).thenReturn(wallet);
        mockMvc.perform(get("/wallets/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(100)); // Проверяем, что возвращается правильный баланс
    }

    @Test
    public void testGetWallet_NotFound() throws Exception {

        when(service.getBalance(walletId)).thenThrow(new IllegalArgumentException("Кошелек не найден"));

        mockMvc.perform(get("/wallets/{walletId}", wall1etId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Кошелек не найден", result.getResolvedException().getMessage()));
    }


}