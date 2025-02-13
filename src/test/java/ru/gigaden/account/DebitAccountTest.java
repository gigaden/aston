package ru.gigaden.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gigaden.exception.DepositAmountException;
import ru.gigaden.exception.WithdrawAmountException;
import ru.gigaden.exception.WithdrawLimitException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DebitAccountTest {
    DebitAccount debitAccount;

    @BeforeEach
    public void before() {
        debitAccount = new DebitAccount(1L, 11L);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldThrowExceptionWhenAmountIsGreaterThanTheWithdrawalBalance(double amount) {
        assertThrows(WithdrawLimitException.class, () -> debitAccount.withdraw(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenAccountIsDeposited(double amount) {
        debitAccount.deposit(amount);
        BigDecimal balance = debitAccount.getBalance();

        assertEquals(0, balance.compareTo(BigDecimal.valueOf(amount)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenAccountIsWithdrawal(double amount) {
        debitAccount.deposit(amount);
        debitAccount.withdraw(amount);
        BigDecimal balance = debitAccount.getBalance();

        assertEquals(0, balance.compareTo(BigDecimal.ZERO));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000.0})
    public void shouldThrowDepositExceptionWhenAmountIsNegative(double amount) {
        assertThrows(DepositAmountException.class, () -> debitAccount.deposit(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000.0})
    public void shouldThrowDepositExceptionWhenWithdrawalAmountIsNegative(double amount) {
        assertThrows(WithdrawAmountException.class, () -> debitAccount.withdraw(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {10000.0})
    public void shouldThrowWithdrawalLimitExcWhenAmountGreaterThenMaxTransactionValue(double amount) {
        debitAccount.deposit(amount);
        double maxAmount = DebitAccount.getMaxTransactionAmount();
        assertThrows(WithdrawLimitException.class, () -> debitAccount.withdraw(maxAmount + 1));
    }

}
