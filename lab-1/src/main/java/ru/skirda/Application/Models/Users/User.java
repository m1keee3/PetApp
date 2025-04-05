package ru.skirda.Application.Models.Users;

import java.math.BigDecimal;

/**
 * Класс, представляющий пользователя.
 */
public class User {
    private final UserRole role;
    private final long id;
    private final long password;
    private BigDecimal balance;

    /**
     * Конструктор класса User.
     *
     * @param role     Роль пользователя.
     * @param id       Идентификатор пользователя.
     * @param password Пароль пользователя.
     * @param balance  Баланс пользователя.
     */
    public User(UserRole role, long id, long password, BigDecimal balance) {
        this.role = role;
        this.id = id;
        this.password = password;
        this.balance = balance;
    }

    /**
     * Возвращает роль пользователя.
     *
     * @return Роль пользователя.
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Возвращает идентификатор пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public long getPassword() {
        return password;
    }

    /**
     * Возвращает баланс пользователя.
     *
     * @return Баланс пользователя.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Устанавливает баланс пользователя.
     *
     * @param balance Новый баланс пользователя.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}