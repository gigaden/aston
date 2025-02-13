package ru.gigaden;

import ru.gigaden.account.BankAccount;
import ru.gigaden.account.CreditAccount;
import ru.gigaden.account.DebitAccount;
import ru.gigaden.account.SavingsAccount;
import ru.gigaden.transaction.TransactionProcessor;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        BankAccount debit = new DebitAccount(1L, 11L);
        BankAccount credit = new CreditAccount(2L, 22L);
        BankAccount saving = new SavingsAccount(3L, 33L);
        debit.deposit(4000);
        saving.deposit(3000);
        List<BankAccount> accountList = new ArrayList<>(List.of(debit, credit, saving));

        TransactionProcessor.processTransaction(accountList, 3000);
        TransactionProcessor.processTransaction(accountList, 1000);
    }
}
