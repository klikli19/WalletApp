package com.klikli.walletapp;

import com.klikli.walletapp.constant.Operation;
import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.exception.WalletNotFoundException;
import com.klikli.walletapp.repository.WalletRepository;
import com.klikli.walletapp.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class WalletServiceTest {

    private WalletRepository walletRepository;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletRepository = Mockito.mock(WalletRepository.class);
    }

    @Test
    void testGetBalanceSuccess() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, 100L); // Создаем кошелек с балансом 100

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Wallet retrievedWallet = walletService.getBalance(walletId);

        assertNotNull(retrievedWallet);
        assertEquals(100L, retrievedWallet.getBalance());
        verify(walletRepository).findById(walletId);
    }

    @Test
    void testGetBalanceWalletNotFound() {
        UUID walletId = UUID.randomUUID();

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(WalletNotFoundException.class, () -> {
            walletService.getBalance(walletId);
        });

        assertNotNull(exception);
        verify(walletRepository).findById(walletId);
    }
    @Test
    void testUpdateBalanceDeposit() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, 100L); // Начальный баланс 100

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updatedWallet = walletService.updateBalance(walletId, Operation.DEPOSIT, 50);

        assertEquals(150L, updatedWallet.getBalance());
        verify(walletRepository).findById(walletId);
        verify(walletRepository).save(wallet);
    }

    @Test
    void testUpdateBalanceWithdraw() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, 100L); // Начальный баланс 100

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updatedWallet = walletService.updateBalance(walletId, Operation.WITHDRAW, 50);

        assertEquals(50L, updatedWallet.getBalance());
        verify(walletRepository).findById(walletId);
        verify(walletRepository).save(wallet);
    }

    @Test
    void testUpdateBalanceWithdrawInsufficientFunds() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, 30L);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Exception exception = assertThrows(NullPointerException.class, () -> {
            walletService.updateBalance(walletId, Operation.WITHDRAW, 50);
        });

        assertEquals("Недостаточно средств", exception.getMessage());
    }

    @Test
    void testUpdateBalanceWalletNotFound() {
        UUID walletId = UUID.randomUUID();

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NullPointerException.class, () -> {
            walletService.updateBalance(walletId, Operation.WITHDRAW, 50);
        });

        assertEquals("Кошелек не найден", exception.getMessage());
    }
}
