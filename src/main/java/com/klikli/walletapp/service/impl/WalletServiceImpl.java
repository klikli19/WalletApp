package com.klikli.walletapp.service.impl;
import com.klikli.walletapp.constant.Operation;
import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.exception.NotEnoughFundsException;
import com.klikli.walletapp.exception.WalletNotFoundException;
import com.klikli.walletapp.repository.WalletRepository;
import com.klikli.walletapp.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final Lock lock = new ReentrantLock();

    public Wallet createWallet(UUID walletId) {
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(0L);
        return walletRepository.save(wallet);
    }

    public Wallet updateBalance(UUID walletId, Operation operationType, double amount) {
        lock.lock();
        try {
            Optional<Wallet> optionalWallet = walletRepository.findById(walletId);

            if (optionalWallet.isEmpty()) {
                throw new WalletNotFoundException();
            }

            Wallet wallet = optionalWallet.get();

            if (operationType == Operation.DEPOSIT) {
                wallet.setBalance(wallet.getBalance() + (long) amount);
            } else if (operationType == Operation.WITHDRAW) {
                if (wallet.getBalance() >= amount) {
                    wallet.setBalance(wallet.getBalance() - (long) amount);
                } else {
                    throw new NotEnoughFundsException();
                }
            }

            return walletRepository.save(wallet);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public Wallet getBalance(UUID walletId) {
        return walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
    }
}

