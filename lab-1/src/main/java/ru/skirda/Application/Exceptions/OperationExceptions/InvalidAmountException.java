package ru.skirda.Application.Exceptions.OperationExceptions;

import java.math.BigDecimal;


/**
 * Исключение для обработки некорректной суммы
 */
public class InvalidAmountException extends OperationException {

    public InvalidAmountException(BigDecimal invalidAmount) {
        super("Invalid amount: " + invalidAmount);
    }
}
