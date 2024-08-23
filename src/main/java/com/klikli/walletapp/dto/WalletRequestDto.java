package com.klikli.walletapp.dto;

import com.klikli.walletapp.constant.Operation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class WalletRequestDto {

    private UUID walletId;

    @NotNull
    private Operation operation;

    @NotNull
    @Positive
    private Integer amount;

}
