package ru.gigaden.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gigaden.exception.DepositAmountException;
import ru.gigaden.exception.WithdrawAmountException;
import ru.gigaden.exception.WithdrawLimitException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreditAccountTest {

    CreditAccount creditAccount;

    @BeforeEach
    public void before() {
        creditAccount = new CreditAccount(1L, 11L);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldThrowExceptionWhenAmountIsGreaterThanTheWithdrawalBalanceWithCreditLimit(double amount) {
        assertThrows(WithdrawLimitException.class,
                () -> creditAccount.withdraw(amount + creditAccount.getCreditLimit()));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenAccountIsDeposited(double amount) {
        creditAccount.deposit(amount);
        BigDecimal balance = creditAccount.getBalance();

        assertEquals(0, balance.compareTo(BigDecimal.valueOf(amount)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenAccountIsWithdrawal(double amount) {
        creditAccount.setWithdrawalFee(0);
        creditAccount.deposit(amount);
        creditAccount.withdraw(amount);
        BigDecimal balance = creditAccount.getBalance();

        assertEquals(0, balance.compareTo(BigDecimal.ZERO));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldThrowExceptionWhenAccountIsWithdrawalWithFeeGreaterThenLimit(double amount) {
        creditAccount.setCreditLimit(0);
        creditAccount.deposit(amount);
        assertThrows(WithdrawLimitException.class, () -> creditAccount.withdraw(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenAccountIsWithdrawalGreaterThenBalanceAndNotMoreThenCreditLimit(double amount) {
        double fee = amount * creditAccount.getWithdrawalFee() / 100;
        BigDecimal expectedBalance = creditAccount.getBalance().subtract(BigDecimal.valueOf(amount + fee));
        creditAccount.withdraw(amount);
        BigDecimal balance = creditAccount.getBalance();

        assertEquals(0, expectedBalance.compareTo(balance));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000.0})
    public void shouldThrowDepositExceptionWhenAmountIsNegative(double amount) {
        assertThrows(DepositAmountException.class, () -> creditAccount.deposit(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000.0})
    public void shouldThrowDepositExceptionWhenWithdrawalAmountIsNegative(double amount) {
        assertThrows(WithdrawAmountException.class, () -> creditAccount.withdraw(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {5000.0})
    public void shouldThrowWithdrawalLimitExcWhenAmountGreaterThenMaxTransactionValue(double amount) {
        double maxAmount = DebitAccount.getMaxTransactionAmount();
        assertThrows(WithdrawLimitException.class, () -> creditAccount.withdraw(maxAmount + 1));
    }

}
