package ru.skirda.Application.Application.Operations;

import ru.skirda.Application.Abstractions.Repositories.IOperationRepository;
import ru.skirda.Application.Contracts.Operations.IOperationService;
import ru.skirda.Application.Models.Operations.Operation;
import ru.skirda.Application.Models.Operations.OperationType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Реализация сервиса для работы с операциями
 */
public class OperationService implements IOperationService {

    private final IOperationRepository repository;

    public OperationService(IOperationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Operation> getOperationsByUserId(long userId) {
        try {
            return repository.getOperationsByUserId(userId);
        } catch (Exception e) {
            System.err.println("Error fetching operations: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void addOperation(long userId, OperationType type, BigDecimal amount)  {
        repository.addOperation(userId, type, amount);
    }
}