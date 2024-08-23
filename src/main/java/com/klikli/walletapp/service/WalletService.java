package com.klikli.walletapp.service;

import com.klikli.walletapp.constant.Operation;
import com.klikli.walletapp.entity.Wallet;

import java.util.UUID;

public interface WalletService {
    Wallet updateBalance(UUID walletId, Operation operationType, double amount);

    Wallet getBalance(UUID walletId);
}
