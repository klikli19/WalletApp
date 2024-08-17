package com.klikli.walletapp.exception;

public class NotEnoughFundsException extends Exception{
    public NotEnoughFundsException(String message) {
        super(message);
    }
}
