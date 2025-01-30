package ru.gigaden.account;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.gigaden.utils.Validator;

import java.math.BigDecimal;

/**
 * Класс BankAccount - базовый, содержит реализацию метода пополнения средств и абстрактный метод для снятия.
 * Хранит id аккаунта, id владельца и баланс.
 * Создаёт объект Logger для наследников - его настройки в logback.xml
 */
@ToString
@Getter
@Slf4j
public abstract class BankAccount {
    protected long accountNumber;
    protected BigDecimal balance;
    protected long accountHolder;

    protected BankAccount(long accountNumber, long accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = BigDecimal.ZERO;
    }

    /**
     * Уменьшает размер баланса.
     *
     * @param amount - сумма для снятия.
     */
    public abstract void withdraw(double amount);

    /**
     * Увеличивает баланс.
     *
     * @param amount - сумма для пополнения.
     */
    public void deposit(double amount) {
        Validator.checkDepositAmount(amount);
        balance = balance.add(BigDecimal.valueOf(amount));
        log.info("На аккаунт id = {} зачислена сумма = {}. Баланс =  {}",
                accountNumber,
                amount,
                balance);
    }
}
