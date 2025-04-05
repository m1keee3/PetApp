package ru.skirda.Application.Contracts.Users;

import ru.skirda.Application.Models.Users.User;

import java.util.Optional;

/**
 * Интерфейс сервиса для работы с текущим пользователем.
 */
public interface ICurrentUserService {

    /**
     * Возвращает текущего пользователя.
     *
     * @return Optional с текущим пользователем или пустой Optional, если пользователь не зарегестрирован.
     */
    Optional<User> getUser();

    /**
     * Устанавливает текущего пользователя.
     *
     * @param user Пользователь, который будет установлен как текущий.
     */
    void setUser(User user);
}