package ru.gigaden.transaction;

import org.junit.jupiter.api.Test;
import ru.gigaden.account.BankAccount;
import ru.gigaden.account.CreditAccount;
import ru.gigaden.account.DebitAccount;
import ru.gigaden.account.SavingsAccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionProcessorTest {

    @Test
    public void shouldBePositiveWhenAllWithdrawalsIsDone() {
        BankAccount debit = new DebitAccount(1L, 11L);
        BankAccount credit = new CreditAccount(2L, 22L);
        BankAccount saving = new SavingsAccount(3L, 33L);
        debit.deposit(4000);
        saving.deposit(3000);
        List<BankAccount> accountList = List.of(debit, credit, saving);

        TransactionProcessor.processTransaction(accountList, 3000);

        assertEquals(0, accountList.get(0).getBalance().compareTo(BigDecimal.valueOf(1000)));
        assertEquals(0, accountList.get(1).getBalance().compareTo(BigDecimal.valueOf(-3030)));
        assertEquals(0, accountList.get(2).getBalance().compareTo(BigDecimal.valueOf(0)));
    }
}