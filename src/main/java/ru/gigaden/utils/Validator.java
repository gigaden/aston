package ru.gigaden.utils;

import lombok.extern.slf4j.Slf4j;
import ru.gigaden.exception.DepositAmountException;
import ru.gigaden.exception.WithdrawAmountException;
import ru.gigaden.exception.WithdrawLimitException;

import java.math.BigDecimal;

/**
 * Класс содержит статические методы валидации
 */
@Slf4j
public final class Validator {

    /**
     * Проверяет размер денежных средств при пополнении
     *
     * @param amount размер средств.
     * @throws DepositAmountException в случае если amount <= 0.
     */
    public static void checkDepositAmount(double amount) {
        if (amount <= 0) {
            log.warn("Попытка пополнить отрицательной суммой.");
            throw new DepositAmountException("Сумма пополнения = %.2f должна быть положительной".formatted(amount));
        }
    }

    /**
     * Проверяет размер денежных средств при снятии без учёта кредитного лимита.
     *
     * @param amount  размер средств.
     * @param balance баланс аккаунта.
     * @throws WithdrawLimitException в случае если amount > balance.
     * @throws DepositAmountException в случае если amount <= 0.
     */
    public static void checkWithdrawLimit(BigDecimal amount, BigDecimal balance) {
        if (amount.compareTo(balance) > 0) {
            log.warn("Попытка снять сумму превышающую лимит.");
            throw new WithdrawLimitException("Сумма = %.2f превышает лимит = %.2f".formatted(amount, balance));
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Попытка снять отрицательную сумму.");
            throw new WithdrawAmountException("Сумма = %.2f должна быть положительной".formatted(amount));
        }
    }

    /**
     * Проверяет размер денежных средств при снятии с учётом кредитного лимита.
     *
     * @param amount      размер средств.
     * @param balance     баланс аккаунта.
     * @param creditLimit кредитный лимит аккаунта.
     * @throws WithdrawLimitException в случае если amount > превышает сумму кредитного лимита и баланса.
     * @throws DepositAmountException в случае если amount <= 0.
     */
    public static void checkWithdrawLimit(BigDecimal amount, BigDecimal balance, double creditLimit) {
        if (amount.compareTo(balance.add(BigDecimal.valueOf(creditLimit))) > 0) {
            log.warn("Попытка снять сумму превышающую лимит с учётом кредитного.");
            throw new WithdrawLimitException("Сумма = %.2f превышает доступный лимит = %.2f"
                    .formatted(amount, balance.doubleValue() + creditLimit)
            );
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Попытка снять отрицательную сумму с учётом кредитного.");
            throw new WithdrawAmountException("Сумма = %.2f должна быть положительной".formatted(amount));
        }
    }
}
