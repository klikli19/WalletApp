package com.klikli.walletapp.exception;

public class NotEnoughFundsException extends RuntimeException{
    public NotEnoughFundsException() {
        super("Недостаточно средств");
    }
}
