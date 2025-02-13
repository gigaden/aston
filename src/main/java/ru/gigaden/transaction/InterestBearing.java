package ru.gigaden.transaction;

/**
 * Интерфейс для начисления процентов на остаток.
 */
public interface InterestBearing {
    /**
     * Вычисляет размер процентов на остаток.
     */
    void applyInterest();
}
