package ru.gigaden.exception;

public class WithdrawalFeeException extends RuntimeException {
    public WithdrawalFeeException(String message) {
        super(message);
    }
}
