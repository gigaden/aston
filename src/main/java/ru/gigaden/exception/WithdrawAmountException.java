package ru.gigaden.exception;

public class WithdrawAmountException extends RuntimeException {
    public WithdrawAmountException(String message) {
        super(message);
    }
}
