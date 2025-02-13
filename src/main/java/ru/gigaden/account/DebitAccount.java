package ru.gigaden.account;

import lombok.extern.slf4j.Slf4j;
import ru.gigaden.exception.WithdrawLimitException;
import ru.gigaden.utils.TransactionValidator;
import ru.gigaden.utils.Validator;

import java.math.BigDecimal;

/**
 * Класс DebitAccount представляет банковский счёт без возможности
 * снимать средства при недостаточном балансе.
 * Также добавляется комиссия за операции снятия средств.
 */
@Slf4j
public class DebitAccount extends BankAccount implements TransactionValidator {
    private static final int MAX_TRANSACTION_AMOUNT = 10_000;

    public DebitAccount(long accountNumber, long accountHolder) {
        super(accountNumber, accountHolder);
    }

    public static int getMaxTransactionAmount() {
        return MAX_TRANSACTION_AMOUNT;
    }

    /**
     * Уменьшает баланс на заданную величину.
     *
     * @param amount размер средств для снятия.
     *               Метод выбрасывает исключение в случае не успешной валидации amount.
     *               Уменьшает баланс на заданную величину.
     */
    @Override
    public void withdraw(double amount) {
        Validator.checkWithdrawLimit(BigDecimal.valueOf(amount), balance);
        /*
         * Сделал проверку, как требуется по ТЗ,
         * но я бы лучше вынес её в Validator.
         * */
        if (validate(amount)) {
            log.warn("Размер транзакции = {} превышает допустимое значение = {}", amount, MAX_TRANSACTION_AMOUNT);
            throw new WithdrawLimitException("Превышен лимит транзакции.");
        }
        balance = balance.subtract(BigDecimal.valueOf(amount));
        log.info("С аккаунта id = {} списана сумма = {}. Баланс =  {}",
                getAccountNumber(),
                amount,
                balance);
    }

    @Override
    public boolean validate(double amount) {
        log.info("Сравниваем размер транзакции с допустимым значением.");
        return amount > MAX_TRANSACTION_AMOUNT;
    }
}
