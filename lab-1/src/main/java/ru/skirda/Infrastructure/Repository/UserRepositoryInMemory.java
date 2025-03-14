package ru.skirda.Infrastructure.Repository;

import ru.skirda.Application.Abstractions.Repositories.IUserRepository;
import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.LoginExceptions.UserAlreadyExistException;
import ru.skirda.Application.Exceptions.OperationExceptions.InsufficientFundsException;
import ru.skirda.Application.Exceptions.OperationExceptions.InvalidAmountException;
import ru.skirda.Application.Models.Users.User;
import ru.skirda.Application.Models.Users.UserRole;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация репозитория пользователей в памяти через HashMap
 */
public class UserRepositoryInMemory implements IUserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void insertUser(long id, long password)
            throws UserAlreadyExistException {
        if (users.containsKey(id)) {
            throw new UserAlreadyExistException(id);
        }
        users.put(id, new User(
                UserRole.USER,
                id,
                password,
                BigDecimal.ZERO
        ));
    }


    @Override
    public void addMoney(long id, BigDecimal amount)
            throws InvalidAmountException, NotFoundException {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount);
        }

        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException(id);
        }

        user.setBalance(user.getBalance().add(amount));
    }

    public synchronized void removeMoney(long id, BigDecimal amount)
            throws InsufficientFundsException, InvalidAmountException, NotFoundException {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount);
        }

        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException(id);
        }

        BigDecimal newBalance = user.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(user.getBalance(), amount);
        }

        user.setBalance(user.getBalance().subtract(amount));
    }

    @Override
    public void clear() {
        users.clear();
    }
}