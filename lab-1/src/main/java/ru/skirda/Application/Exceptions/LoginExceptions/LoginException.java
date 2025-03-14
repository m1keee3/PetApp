package ru.skirda.Application.Exceptions.LoginExceptions;

/**
 * Базовое исключение для операций авторизации
 */
public abstract class LoginException extends Exception {
    public LoginException(String message) {
        super(message);
    }
}