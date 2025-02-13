package ru.gigaden.utils;

/**
 * Содержит методы для валидации транзакций.
 */
public interface TransactionValidator {
    boolean validate(double amount);
}
