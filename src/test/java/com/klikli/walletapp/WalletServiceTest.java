package com.klikli.walletapp;

import com.klikli.walletapp.entity.Wallet;
import com.klikli.walletapp.repository.WalletRepository;
import com.klikli.walletapp.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        walletId = UUID.randomUUID();
        wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(100L);
    }
    @Test
    public void testGetBalance_Success() {
        UUID walletId = UUID.randomUUID();
        Wallet expectedWallet = new Wallet();
        expectedWallet.setWalletId(walletId);
        expectedWallet.setBalance(100L);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(expectedWallet));
        Wallet actualWallet = walletService.getBalance(walletId);
        assertNotNull(actualWallet);
        assertEquals(walletId, actualWallet.getWalletId());
        assertEquals(100L, actualWallet.getBalance());
        verify(walletRepository).findById(walletId);
    }

    @Test
    public void testGetBalance_ThrowsException_WhenWalletNotFound() {
        UUID walletId = UUID.randomUUID();
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            walletService.getBalance(walletId);
        });
        assertEquals("Кошелек не найден", exception.getMessage());
        verify(walletRepository).findById(walletId);
    }

    @Test
    public void testUpdateBalance_Deposit_Success() {
        double depositAmount = 50.0;
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet updatedWallet = walletService.updateBalance(walletId, "DEPOSIT", depositAmount);

        assertNotNull(updatedWallet);
        assertEquals(150L, updatedWallet.getBalance());
        verify(walletRepository).findById(walletId);
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testUpdateBalance_Withdraw_Success() {
        double withdrawAmount = 30.0;
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet updatedWallet = walletService.updateBalance(walletId, "WITHDRAW", withdrawAmount);

        assertNotNull(updatedWallet);
        assertEquals(70L, updatedWallet.getBalance());
        verify(walletRepository).findById(walletId);
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testUpdateBalance_Withdraw_InsufficientFunds() {
        double withdrawAmount = 70.0;
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Wallet withdraw = walletService.updateBalance(walletId, "WITHDRAW", withdrawAmount);
        });

        assertEquals("Недостаточно средств", exception.getMessage());
        verify(walletRepository).findById(walletId);
        verify(walletRepository, never()).save(any(Wallet.class));
    }
}
