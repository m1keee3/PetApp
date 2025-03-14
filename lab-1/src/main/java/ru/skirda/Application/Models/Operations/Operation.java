package ru.skirda.Application.Models.Operations;

import java.math.BigDecimal;

/**
 * Класс, представляющий операцию
 */
public class Operation {
    private final long accountId;
    private final OperationType operationType;
    private final BigDecimal amount;

    /**
     * Конструктор класса Operation
     *
     * @param accountId идентификатор пользователя
     * @param operationType тип операции
     * @param amount сумма операции
     */
    public Operation(long accountId, OperationType operationType, BigDecimal amount) {
        this.accountId = accountId;
        this.operationType = operationType;
        this.amount = amount;
    }

    public long getAccountId() {
        return accountId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.valueOf(accountId) +
                ' ' +
                operationType +
                ' ' +
                amount.stripTrailingZeros().toPlainString();
    }
}