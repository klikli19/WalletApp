package com.klikli.walletapp.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.Objects;
import java.util.UUID;
@Entity
@Builder
@Table(name = "wallets")
public class Wallet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletId;
    @Column(name = "balance")
    private Long balance;
    public Wallet(UUID walletId, Long balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public Wallet() {
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(walletId, wallet.walletId) && Objects.equals(balance, wallet.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, balance);
    }
}
