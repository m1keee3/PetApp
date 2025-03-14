package ru.skirda.Application.Exceptions.OperationExceptions;

import java.math.BigDecimal;

/**
 * Исключение для обработки случая, когда на счете недостаточно средств
 */
public class InsufficientFundsException extends OperationException {

    public InsufficientFundsException(BigDecimal currentBalance, BigDecimal requiredAmount) {
        super(String.format("Insufficient funds. Current: %s, Required: %s",
                currentBalance, requiredAmount));
    }
}
