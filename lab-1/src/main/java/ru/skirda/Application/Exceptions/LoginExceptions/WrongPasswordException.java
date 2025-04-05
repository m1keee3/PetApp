package ru.skirda.Application.Exceptions.LoginExceptions;

/**
 * Исключение для обработки неверного пароля
 */
public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Неверный пароль");
    }
}
