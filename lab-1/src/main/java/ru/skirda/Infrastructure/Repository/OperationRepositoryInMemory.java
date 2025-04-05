package ru.skirda.Infrastructure.Repository;

import ru.skirda.Application.Abstractions.Repositories.IOperationRepository;
import ru.skirda.Application.Models.Operations.Operation;
import ru.skirda.Application.Models.Operations.OperationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация репозитория операций в памяти через HashMap
 */
public class OperationRepositoryInMemory implements IOperationRepository {
    private final Map<Long, List<Operation>> storage = new HashMap<>();


    @Override
    public synchronized List<Operation> getOperationsByUserId(long userId) {
        return new ArrayList<>(storage.getOrDefault(userId, List.of()));
    }

    public void addOperation(long userId, OperationType type, BigDecimal amount) {
        List<Operation> userOperations = storage.computeIfAbsent(userId, k -> new ArrayList<>());
        userOperations.add(new Operation(userId, type, amount));
    }
}