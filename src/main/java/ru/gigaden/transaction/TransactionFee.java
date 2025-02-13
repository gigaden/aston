package ru.gigaden.transaction;

/**
 * Интерфейс для начисления комиссии на денежные средства.
 */
public interface TransactionFee {
    /**
     * Вычисляет размер комиссии.
     *
     * @param amount сумма для расчёта комиссии.
     * @return сумма с учётом комиссии.
     */
    double applyFee(double amount);
}
