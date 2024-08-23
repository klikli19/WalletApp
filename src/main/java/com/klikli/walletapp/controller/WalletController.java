package com.klikli.walletapp.controller;

import com.klikli.walletapp.dto.WalletRequestDto;
import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.service.impl.WalletServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class WalletController {
    private final WalletServiceImpl service;

    public WalletController(WalletServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public Wallet updateWallet(@RequestBody WalletRequestDto request) {
        return service.updateBalance(request.getWalletId(), request.getOperation(), request.getAmount());
    }

    @GetMapping("/wallets/{walletId}")
    public Wallet getWallet(@PathVariable UUID walletId) {
        return service.getBalance(walletId);
    }
}
