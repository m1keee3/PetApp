package ru.skirda.Application.Contracts.Operations;

import ru.skirda.Application.Models.Operations.Operation;
import ru.skirda.Application.Models.Operations.OperationType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Интерфейс сервиса для работы с операциями.
 */
public interface IOperationService {

    /**
     * Получает список операций по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список операций, связанных с пользователем
     */
    List<Operation> getOperationsByUserId(long userId);

    /**
     * Добавляет операцию в список
     *
     * @param userId идентификатор пользователя
     * @param type тип операции
     * @param amount сумма операции
     */
    void addOperation(long userId, OperationType type, BigDecimal amount);
}