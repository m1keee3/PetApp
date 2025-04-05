package ru.skirda.Application.Exceptions.LoginExceptions;

/**
 * Исключение для обработки неправильного ввода имени администратора
 */
public class WrongAdminNameException extends RuntimeException {
    public WrongAdminNameException(String message) {
        super("Admin with name '" + message + "' not found");
    }
}
