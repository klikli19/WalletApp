package com.klikli.walletapp.exception;

public class WalletNotFoundException extends RuntimeException{

    public WalletNotFoundException() {
        super("Кошелек не найден. Проверьте данные");
    }
}
