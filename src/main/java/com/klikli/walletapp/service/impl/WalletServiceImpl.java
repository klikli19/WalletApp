package com.klikli.walletapp.service.impl;
import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.repository.WalletRepository;
import com.klikli.walletapp.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    public Wallet createWallet(UUID walletId) {
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(0L);
        return walletRepository.save(wallet);
    }

    public Wallet updateBalance(UUID walletId, String operationType, double amount) {
        Optional<Object> optionalWallet = Optional.of(walletRepository.findById(walletId));
        Wallet wallet = (Wallet) optionalWallet.get();
        if ("DEPOSIT".equalsIgnoreCase(operationType)) {
            wallet.setBalance((long) (wallet.getBalance() + amount));
        } else if ("WITHDRAW".equalsIgnoreCase(operationType)) {
            if (wallet.getBalance() >= amount) {
                wallet.setBalance((long) (wallet.getBalance() - amount));
            } else {
                throw new IllegalArgumentException("Insufficient funds");
            }
        }
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getBalance(UUID walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    }
}

