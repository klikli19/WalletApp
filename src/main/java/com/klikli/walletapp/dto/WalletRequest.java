package com.klikli.walletapp.dto;

import com.klikli.walletapp.constant.Operation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {
    private UUID walletId;
    @NotNull
    private Operation operation;
    @NotNull
    @Positive
    private Integer amount;
}
