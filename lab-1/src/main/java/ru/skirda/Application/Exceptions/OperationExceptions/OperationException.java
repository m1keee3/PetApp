package ru.skirda.Application.Exceptions.OperationExceptions;


/**
 * Базовое исключение для операций
 */
public abstract class OperationException extends Exception {
  public OperationException(String message) {
    super(message);
  }
}

