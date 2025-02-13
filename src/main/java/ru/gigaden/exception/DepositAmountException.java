package ru.gigaden.exception;

public class DepositAmountException extends RuntimeException {
    public DepositAmountException(String message) {
        super(message);
    }
}
