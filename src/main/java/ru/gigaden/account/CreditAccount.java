package ru.gigaden.account;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.gigaden.exception.CreditLimitException;
import ru.gigaden.exception.WithdrawLimitException;
import ru.gigaden.exception.WithdrawalFeeException;
import ru.gigaden.transaction.TransactionFee;
import ru.gigaden.utils.TransactionValidator;
import ru.gigaden.utils.Validator;

import java.math.BigDecimal;

/**
 * Класс CreditAccount представляет банковский счёт с возможностью
 * ухода в кредит до установленного лимита.
 * Также добавляется комиссия за операции снятия средств.
 */
@Getter
@Slf4j
public class CreditAccount extends BankAccount implements TransactionFee, TransactionValidator {
    private double creditLimit = 5000;
    private double withdrawalFee = 1;
    private static final int MAX_TRANSACTION_AMOUNT = 5_000;

    /**
     * Создаёт экземпляр кредитного счёта.
     *
     * @param accountNumber Уникальный номер счёта.
     * @param accountHolder Идентификатор владельца счёта.
     */
    public CreditAccount(long accountNumber, long accountHolder) {
        super(accountNumber, accountHolder);
    }

    /**
     * Устанавливает новый лимит кредита.
     *
     * @param creditLimit Новый лимит кредита. Должен быть ≥ 0.
     * @throws CreditLimitException Если переданный лимит меньше 0.
     */
    public void setCreditLimit(double creditLimit) {
        if (creditLimit < 0) {
            log.warn("Попытка установить отрицательную кредитный лимит");
            throw new CreditLimitException("Кредитный лимит не может быть отрицательным");
        }
        this.creditLimit = creditLimit;
    }

    /**
     * Устанавливает новую комиссию за снятие средств.
     *
     * @param withdrawalFee Новая комиссия за снятие средств в процентах.
     * @throws WithdrawalFeeException Если переданное значение меньше 0.
     */
    public void setWithdrawalFee(double withdrawalFee) {
        if (withdrawalFee < 0) {
            log.warn("Попытка установить отрицательную комиссию за снятие");
            throw new WithdrawalFeeException("Комиссия за снятие не может быть отрицательной");
        }
        this.withdrawalFee = withdrawalFee;
    }

    /**
     * Снимает средства с учётом кредитного лимита и комиссии.
     *
     * @param amount Сумма для снятия.
     */
    @Override
    public void withdraw(double amount) {
        double amountWithFee = applyFee(amount);
        Validator.checkWithdrawLimit(BigDecimal.valueOf(amountWithFee), balance, creditLimit);
        /*
         * Сделал проверку, как требуется по ТЗ,
         * но я бы лучше вынес её в Validator.
         * */
        if (validate(amountWithFee)) {
            log.warn("Размер транзакции = {} превышает допустимое значение = {}", amountWithFee, MAX_TRANSACTION_AMOUNT);
            throw new WithdrawLimitException("Превышен лимит транзакции.");
        }
        balance = balance.subtract(BigDecimal.valueOf(amountWithFee));
        log.info("С аккаунта id = {} списана сумма с учётом комиссии = {}. Баланс =  {}",
                getAccountNumber(),
                amountWithFee,
                balance);
    }

    /**
     * Рассчитывает сумму с учётом комиссии.
     *
     * @param amount Исходная сумма.
     * @return Сумма с добавленной комиссией.
     */
    @Override
    public double applyFee(double amount) {
        amount += amount * withdrawalFee / 100;
        return amount;
    }

    @Override
    public boolean validate(double amount) {
        log.info("Сравниваем размер транзакции с допустимым значением.");
        return amount > MAX_TRANSACTION_AMOUNT;
    }
}
