package ru.skirda.Application.Abstractions.Repositories;

import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.LoginExceptions.UserAlreadyExistException;
import ru.skirda.Application.Exceptions.OperationExceptions.InsufficientFundsException;
import ru.skirda.Application.Exceptions.OperationExceptions.InvalidAmountException;
import ru.skirda.Application.Models.Users.User;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с пользователями.
 */
public interface IUserRepository {

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional с пользователем или пустой Optional, если пользователь не найден
     */
    Optional<User> findUserById(long id);

    /**
     * Создает нового пользователя.
     *
     * @param id идентификатор пользователя
     * @param password пароль пользователя
     * @throws UserAlreadyExistException если пользователь уже существует
     */
    void insertUser(long id, long password) throws UserAlreadyExistException;


    /**
     * Добавляет деньги на счет пользователя.
     *
     * @param id идентификатор пользователя
     * @param amount сумма для добавления
     * @throws InvalidAmountException если введена некорректная сумма
     * @throws NotFoundException если user с таким id не был найден
     */
    void addMoney(long id, BigDecimal amount) throws InvalidAmountException, NotFoundException;


    /**
     * Снимает деньги со счета пользователя.
     *
     * @param id Идентификатор пользователя.
     * @param amount Сумма для снятия.
     * @throws InsufficientFundsException если сумма снятия превышает сумму счета
     * @throws InvalidAmountException если введена некорректная сумма
     * @throws NotFoundException если user с таким id не был найден
     */
    void removeMoney(long id, BigDecimal amount) throws InsufficientFundsException, InvalidAmountException, NotFoundException;

    /**
     * Удаляет всех пользователей из репозитория
     */
    void clear();
}