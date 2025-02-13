package ru.gigaden.account;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.gigaden.exception.InterestLimitException;
import ru.gigaden.exception.InterestPeriodException;
import ru.gigaden.transaction.InterestBearing;
import ru.gigaden.utils.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Класс SavingsAccount представляет банковский счёт с возможностью
 * начисления процентов на остаток.
 * Начисление происходит один раз в месяц.
 */
@Setter
@Slf4j
public class SavingsAccount extends BankAccount implements InterestBearing {
    private double monthlyInterest = 3;
    private LocalDate lastInterestPaid;

    public SavingsAccount(long accountNumber, long accountHolder) {
        super(accountNumber, accountHolder);
        this.lastInterestPaid = LocalDate.now();
    }

    /**
     * Устанавливает размер процента на остаток
     *
     * @param monthlyInterest величина процента на остаток.
     * @throws InterestLimitException если размер процентов < 0.
     */
    public void setMonthlyInterest(double monthlyInterest) {
        if (monthlyInterest < 0) {
            log.warn("Попытка установить отрицательные проценты");
            throw new InterestLimitException("Проценты не могут быть отрицательными");
        }
        this.monthlyInterest = monthlyInterest;
    }

    /**
     * Начисляет процент на остаток, проверяя, что с прошлого начисления
     * прошло не меньше месяца.
     * Обновляет дату последнего начисления процентов.
     */
    @Override
    public void applyInterest() {
        /* Хз как это сделать правильно, решил записывать дату последнего начисления процентов и проверять,
         * что не прошёл месяц между последним и следующим начислением*/
        checkInterestPeriod(lastInterestPaid, LocalDate.now());
        balance = balance
                .add(balance
                        .multiply(BigDecimal.valueOf(monthlyInterest)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)); //balance * monthlyInterest / 100;
        lastInterestPaid = LocalDate.now();
        log.info("На аккаунт id = {} начислены проценты, баланс = {}",
                getAccountNumber(),
                balance);
    }

    /**
     * Уменьшает размер баланса на заданное значения.
     *
     * @param amount величина для уменьшения баланса
     */
    @Override
    public void withdraw(double amount) {
        Validator.checkWithdrawLimit(BigDecimal.valueOf(amount), balance);
        balance = balance.subtract(BigDecimal.valueOf(amount));
        log.info("С аккаунта id = {} списана сумма. Баланс =  {}",
                getAccountNumber(),
                balance);
    }

    /**
     * Проверяет, что после последнего начисления процентов прошёл месяц.
     *
     * @param lastDate    дата последнего начисления процентов.
     * @param currentDate дата предполагаемого начисления процентов.
     * @throws InterestPeriodException в случае, если между двумя датами меньше месяца.
     */
    public void checkInterestPeriod(LocalDate lastDate, LocalDate currentDate) {
        if (ChronoUnit.MONTHS.between(lastDate, currentDate) < 1)
            throw new InterestPeriodException("Прошло меньше месяца с последнего начисления");
    }
}
