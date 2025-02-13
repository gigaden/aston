package ru.gigaden.transaction;

import lombok.extern.slf4j.Slf4j;
import ru.gigaden.account.BankAccount;
import ru.gigaden.exception.WithdrawLimitException;

import java.util.List;

@Slf4j
public final class TransactionProcessor {

    /**
     * @param amount   размер суммы для снятия.
     * @param accounts список счетов.
     *                 Для каждого счета вызывает withdraw(amount).
     *                 Использует полиморфизм: метод работает с любыми
     *                 наследниками BankAccount.
     */
    public static void processTransaction(List<BankAccount> accounts, double amount) {
        log.info("Начат процесс списания средств со счетов.");
        accounts.forEach(account -> {
            try {
                account.withdraw(amount);
            } catch (WithdrawLimitException e) {
                log.info("Ошибка списания со счёта = {}: {}", account.getAccountNumber(), e.getMessage());
            }
        });
        log.info("Списание со счетов закончено.");
    }
}
