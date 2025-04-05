package ru.skirda.Application.Application.Users;

import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Users.User;

import java.util.Optional;

/**
 * Реализация сервиса для работы с текущим пользователем
 */
public class CurrentUserManager implements ICurrentUserService {
    private Optional<User> user = Optional.empty();

    @Override
    public Optional<User> getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = Optional.ofNullable(user);
    }
}