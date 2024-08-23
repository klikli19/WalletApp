package com.klikli.walletapp;

import static java.lang.reflect.Array.get;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klikli.walletapp.constant.Operation;
import com.klikli.walletapp.controller.WalletController;
import com.klikli.walletapp.dto.WalletRequestDto;
import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@WebMvcTest(controllers = WalletController.class)
public class WalletControllerTest {
    @Mock
    private WalletServiceImpl service;

    @InjectMocks
    private WalletController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testUpdateWallet() throws Exception {
        WalletRequestDto requestDto = new WalletRequestDto(UUID.randomUUID(), Operation.DEPOSIT, 100);
        Wallet expectedWallet = new Wallet();
        when(service.updateBalance(any(UUID.class), Operation.valueOf(anyString()), anyDouble())).thenReturn(expectedWallet);

        mockMvc.perform(post("/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedWallet.getBalance()));
    }

    @Test
    public void testGetWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet expectedWallet = new Wallet();
        when(service.getBalance(walletId)).thenReturn(expectedWallet);

        mockMvc.perform(get("/wallets/{walletId}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedWallet.getBalance()));
    }

}