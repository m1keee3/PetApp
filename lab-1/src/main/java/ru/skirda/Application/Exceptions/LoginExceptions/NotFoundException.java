package ru.skirda.Application.Exceptions.LoginExceptions;

/**
 * Исключение для обработки случая, когда пользователь не найден
 */
public class NotFoundException extends LoginException {
    public NotFoundException(long userId) {
        super("Пользователь с ID " + userId + " не найден");
    }
}
