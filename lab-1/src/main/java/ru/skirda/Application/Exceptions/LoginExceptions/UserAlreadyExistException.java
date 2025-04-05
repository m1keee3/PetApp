package ru.skirda.Application.Exceptions.LoginExceptions;

/**
 * Исключение для обработки коллизии при создании пользователя
 */
public class UserAlreadyExistException extends LoginException {
    public UserAlreadyExistException(long id) {
        super("User with id " + id + " already exists");
    }
}
