package ru.skirda.Application.Abstractions.Repositories;

import ru.skirda.Application.Models.Operations.Operation;
import ru.skirda.Application.Models.Operations.OperationType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Интерфейс репозитория для работы с операциями.
 */
public interface IOperationRepository {

    /**
     * Получает список операций пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список операций
     */
    List<Operation> getOperationsByUserId(long userId);

    /**
     * Добавляет новую операцию.
     *
     * @param userId идентификатор пользователя
     * @param type тип операции
     * @param amount сумма операции
     */
    void addOperation(long userId, OperationType type, BigDecimal amount);
}