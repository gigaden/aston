package ru.gigaden.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gigaden.exception.DepositAmountException;
import ru.gigaden.exception.InterestPeriodException;
import ru.gigaden.exception.WithdrawAmountException;
import ru.gigaden.exception.WithdrawLimitException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SavingsAccountTest {


    SavingsAccount savingsAccount = new SavingsAccount(1L, 11L);

    @Test
    public void shouldThrowExceptionWhenAmountIsGreaterThanTheWithdrawalBalance() {
        double amount = 1000;
        assertThrows(WithdrawLimitException.class, () -> savingsAccount.withdraw(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenAccountIsDeposited(double amount) {
        savingsAccount.deposit(amount);
        BigDecimal balance = savingsAccount.getBalance();

        assertEquals(0, balance.compareTo(BigDecimal.valueOf(amount)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenAccountIsWithdrawal(double amount) {
        savingsAccount.deposit(amount);
        savingsAccount.withdraw(amount);
        BigDecimal balance = savingsAccount.getBalance();

        assertEquals(0, balance.compareTo(BigDecimal.ZERO));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000.0})
    public void shouldThrowDepositExceptionWhenAmountIsNegative(double amount) {
        assertThrows(DepositAmountException.class, () -> savingsAccount.deposit(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000.0})
    public void shouldThrowDepositExceptionWhenWithdrawalAmountIsNegative(double amount) {
        assertThrows(WithdrawAmountException.class, () -> savingsAccount.withdraw(amount));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBeThrownExceptionWhenApplyInterestLessThenOneMonth(double amount) {
        savingsAccount.deposit(amount);
        assertThrows(InterestPeriodException.class, () -> savingsAccount.applyInterest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {1000.0})
    public void shouldBePositiveWhenApplyInterest(double amount) {
        double monthlyInterest = 50;
        BigDecimal expected = BigDecimal.valueOf(amount + amount * monthlyInterest / 100);
        savingsAccount.setLastInterestPaid(LocalDate.now().minusMonths(1));
        savingsAccount.deposit(amount);
        savingsAccount.setMonthlyInterest(monthlyInterest);
        savingsAccount.applyInterest();
        assertEquals(0, expected.compareTo(savingsAccount.getBalance()));
    }

}
