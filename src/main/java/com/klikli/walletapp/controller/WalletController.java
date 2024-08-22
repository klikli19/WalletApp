package com.klikli.walletapp.controller;

import com.klikli.walletapp.constant.Operation;
import com.klikli.walletapp.dto.WalletRequestDto;
import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.service.impl.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class WalletController {
    @Autowired
    private WalletServiceImpl service;

    @PostMapping
    public Wallet updateWallet(@RequestBody WalletRequestDto request) {
        return service.updateBalance(request.getWalletId(), request.getOperation(), request.getAmount());
    }

    @GetMapping("/wallets/{walletId}")
    public Wallet getWallet(@PathVariable UUID walletId) {
        return service.getBalance(walletId);
    }
}
